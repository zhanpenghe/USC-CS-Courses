/*============================================================================
==============================================================================

                              cubic_spline_task.c

==============================================================================
Remarks:

      sekeleton to create the sample task

============================================================================*/
#include <math.h>
#include <iostream>
using namespace std;
// system headers
#include "SL_system_headers.h"

/* SL includes */
#include "SL.h"
#include "SL_user.h"
#include "SL_tasks.h"
#include "SL_task_servo.h"
#include "SL_kinematics.h"
#include "SL_dynamics.h"
#include "SL_collect_data.h"
#include "SL_shared_memory.h"
#include "SL_man.h"
#include "SL_filters.h"

// defines

// local variables
static double      tau = 16.0;
static double      movement_time = 2000;
static Filter     *fthdd;
static double     *cart;
static int        *cstatus;
static double      start_time = 0.0;
static SL_DJstate *target;
static SL_Cstate  *ctarget;
static SL_Cstate  *cnext;
static double      time_step = 0.01;

int axis_time = 4; // the time to finish an axis for the square
int axis_id = 1;
int tick = 0;


// global functions
extern "C" void
add_cubic_spline_task( void );

// local functions
static int  init_cubic_spline_task(void);
static int  run_cubic_spline_task(void);
static int  change_cubic_spline_task(void);

static int 
cubic_spline_next_step (double x,double xd, double xdd, double t, double td, double tdd,
      double t_togo, double dt,
      double *x_next, double *xd_next, double *xdd_next);
/*****************************************************************************
******************************************************************************
Function Name : add_cubic_spline_task
Date    : Feb 1999
Remarks:

adds the task to the task menu

******************************************************************************
Paramters:  (i/o = input/output)

none

*****************************************************************************/
void
add_cubic_spline_task( void )
{
  int i, j;

  cart    = my_vector(1,n_endeffs*6);
  ctarget = (SL_Cstate *) my_calloc(n_endeffs+1,sizeof(SL_Cstate),MY_STOP);
  cnext   = (SL_Cstate *) my_calloc(n_endeffs+1,sizeof(SL_Cstate),MY_STOP);
  cstatus = my_ivector(1,n_endeffs*6);
  target  = (SL_DJstate *)my_calloc(n_dofs+1,sizeof(SL_DJstate),MY_STOP);
  fthdd   = (Filter *)my_calloc(n_dofs+1,sizeof(Filter),MY_STOP);

  addTask("Cubic Spline Task", init_cubic_spline_task,
          run_cubic_spline_task, change_cubic_spline_task);

}

/*****************************************************************************
******************************************************************************
  Function Name : init_cubic_spline_task
  Date    : Dec. 1997

  Remarks:

  initialization for task

******************************************************************************
  Paramters:  (i/o = input/output)

       none

 *****************************************************************************/
static int
init_cubic_spline_task(void)
{
  int j, i;
  int ans;
  static int firsttime = TRUE;
  time_step = 1./(double)task_servo_rate;


  if (firsttime){
    vec_zero(cart);
    ivec_zero(cstatus);
    bzero((char *)&(ctarget[1]),n_endeffs*sizeof(ctarget[1]));

    firsttime = FALSE;
  }

  /* the cnext state is the desired state as seen form this program */
  for (i=1; i<=n_endeffs;++i) {
    cnext[i] = cart_des_state[i];
  }

  /* zero the filters */
  for (i=1; i<=n_dofs; ++i)
    for (j=0; j<=FILTER_ORDER; ++j)
      fthdd[i].raw[j] = fthdd[i].filt[j] = 0;

  // prepare going to the default posture
  bzero((char *)&(target[1]),N_DOFS*sizeof(target[1]));
  for (i=1; i<=N_DOFS; i++)
    target[i] = joint_default_state[i];

  // go to the target using inverse dynamics (ID)
  if (!go_target_wait_ID(target))
    return FALSE;

  // re-use the variable target for our min-jerk movement: only the left arm moves
  for (i=1; i<=n_endeffs; ++i) {
    ctarget[i].x[_X_] = cart_des_state[i].x[_X_];
    ctarget[i].x[_Y_] = cart_des_state[i].x[_Y_];
    ctarget[i].x[_Z_] = cart_des_state[i].x[_Z_];
  }

  ctarget[2].x[_X_] = -0.16554;
  ctarget[2].x[_Y_] = 0.10235;
  ctarget[2].x[_Z_] = -0.045;


  // enable using left hand...
  cstatus[6 + _X_] = 1;
  cstatus[6 + _Y_] = 1;
  cstatus[6 + _Z_] = 1;

  // ready to go
  ans = 999;
  while (ans == 999) {
    if (!get_int("Enter 1 to start or anthing else to abort ...",ans,&ans))
      return FALSE;
  }

  // only go when user really types the right thing
  if (ans != 1)
    return FALSE;

  start_time = task_servo_time;
  printf("start time = %.3f, task_servo_time = %.3f\n",
         start_time, task_servo_time);

  // start data collection
  scd();

  // time to go
  tau = movement_time;

  return TRUE;
}

/*****************************************************************************
******************************************************************************
  Function Name : run_cubic_spline_task
  Date    : Dec. 1997

  Remarks:

  run the task from the task servo: REAL TIME requirements!

******************************************************************************
  Paramters:  (i/o = input/output)

  none

 *****************************************************************************/
static int
run_cubic_spline_task(void)
{
  int j, i;

  double aux;

  if (tau <= -0.5) {
      freeze();
      return TRUE;
    }

  // compute the update for the desired states
  double t = movement_time - tau;
  
  // to determine current target state and the time left to finish the current axis
  double axis_time_left = (double)axis_time - (t - axis_time*((int)(t/axis_time)));
  
  double y_target = 0.10235;

  switch(axis_id){
    case 0:
      ctarget[LEFT_HAND].x[_X_] = -0.16554;
      ctarget[LEFT_HAND].x[_Z_] = -0.08703;
      ctarget[LEFT_HAND].x[_Y_] = y_target;
      break;
    case 1:
      ctarget[LEFT_HAND].x[_X_] = -0.12554;
      ctarget[LEFT_HAND].x[_Z_] = -0.08703;
      ctarget[LEFT_HAND].x[_Y_] = y_target;
      break;
    case 2:
      ctarget[LEFT_HAND].x[_X_] = -0.12554;
      ctarget[LEFT_HAND].x[_Z_] = -0.04703;
      ctarget[LEFT_HAND].x[_Y_] = y_target;
      break;
    case 3:
      ctarget[LEFT_HAND].x[_X_] = -0.16554;
      ctarget[LEFT_HAND].x[_Z_] = -0.04703;
      ctarget[LEFT_HAND].x[_Y_] = y_target;
      break;
    default:
      break;
  }

  if(axis_time_left <= time_step){
    // printf("[INFO] Catch time of 0! time left: %.2f. Current time: %.2f\n", axis_time_left, t);
    axis_time_left = axis_time;
    axis_id = (axis_id+1)%4;
    //printf("[INFO] Switch to task: %d, time left updated: %f\n", axis_id, axis_time_left);
    //printf("[INFO] Current task: (X_TARGET, Z_TARGET)=(%.2f, %.2f)\n", x_target, z_target);
  }

  tick += 1;

  double x_next=0.0, x_next_d=0.0, x_next_dd=0.0;
  double z_next=0.0, z_next_d=0.0, z_next_dd=0.0;
  double y_next=0.0, y_next_d=0.0, y_next_dd=0.0;

  cubic_spline_next_step (cnext[LEFT_HAND].x[_X_], cnext[LEFT_HAND].xd[_X_], cnext[LEFT_HAND].xdd[_X_], ctarget[LEFT_HAND].x[_X_], 0.0, 0.0,
      axis_time_left, time_step, &x_next, &x_next_d, &x_next_dd);
  cubic_spline_next_step (cnext[LEFT_HAND].x[_Z_], cnext[LEFT_HAND].xd[_Z_], cnext[LEFT_HAND].xdd[_Z_], ctarget[LEFT_HAND].x[_Z_], 0.0, 0.0,
      axis_time_left, time_step, &z_next, &z_next_d, &z_next_dd);

  cubic_spline_next_step (cnext[LEFT_HAND].x[_Y_], cnext[LEFT_HAND].xd[_Y_], cnext[LEFT_HAND].xdd[_Y_], ctarget[LEFT_HAND].x[_Y_], 0.0, 0.0,
      axis_time_left, time_step, &y_next, &y_next_d, &y_next_dd);

  cnext[LEFT_HAND].x[_X_] = x_next;
  cnext[LEFT_HAND].x[_Z_] = z_next;
  cnext[LEFT_HAND].x[_Y_] = y_next;
  cnext[LEFT_HAND].xd[_X_] = x_next_d;
  cnext[LEFT_HAND].xd[_Z_] = z_next_d;
  cnext[LEFT_HAND].xd[_Y_] = y_next_d;
  cnext[LEFT_HAND].xdd[_X_] = x_next_dd;
  cnext[LEFT_HAND].xdd[_Z_] = z_next_dd;
  cnext[LEFT_HAND].xdd[_Y_] = y_next_dd;

  tau -= time_step;

  /* shuffle the target for the inverse kinematics */
  for (i=1; i<=n_endeffs; ++i) {
    for (j=1; j<=N_CART; ++j) {
      aux  = cnext[i].x[j] - cart_des_state[i].x[j];
      cart[(i-1)*6+j] = cnext[i].xd[j] + 20.*aux;
    }
  }

  /* inverse kinematics */
  for (i=1; i<=n_dofs; ++i) {
    target[i].th = joint_des_state[i].th;
  }
  if (!inverseKinematics(target,endeff,joint_opt_state,
                         cart,cstatus,time_step)) {
    freeze();
    return FALSE;
  }

  /* prepare inverse dynamics */
  for (i=1; i<=n_dofs; ++i) {
    aux = (target[i].thd-joint_des_state[i].thd)*(double)task_servo_rate;
    target[i].thdd  = filt(aux,&(fthdd[i]));

    joint_des_state[i].thdd = target[i].thdd;
    joint_des_state[i].thd  = target[i].thd;
    joint_des_state[i].th   = target[i].th;

    if (joint_des_state[i].th > joint_range[i][MAX_THETA]) {
      joint_des_state[i].th = joint_range[i][MAX_THETA];
      joint_des_state[i].thd = 0.0;
      joint_des_state[i].thdd = 0.0;
    }
    if (joint_des_state[i].th < joint_range[i][MIN_THETA]) {
      joint_des_state[i].th = joint_range[i][MIN_THETA];
      joint_des_state[i].thd = 0.0;
      joint_des_state[i].thdd = 0.0;
    }
  }

  // compute inverse dynamics torques
  SL_InvDynNE(joint_state,joint_des_state,endeff,&base_state,&base_orient);

  return TRUE;
}

/*****************************************************************************
******************************************************************************
  Function Name : change_cubic_spline_task
  Date    : Dec. 1997

  Remarks:

  changes the task parameters

******************************************************************************
  Paramters:  (i/o = input/output)

  none

 *****************************************************************************/
static int
change_cubic_spline_task(void)
{
  int    ivar;
  double dvar;

  return TRUE;

}


/*!*****************************************************************************
 *******************************************************************************
\note  cubic_spline_next_step
\date  April 2014

\remarks

Given the time to go, the current state is updated to the next state
using min jerk splines

 ******************************************************************************/

static int 
cubic_spline_next_step (double x,double xd, double xdd, double t, double td, double tdd,
      double t_togo, double dt,
      double *x_next, double *xd_next, double *xdd_next)

{
  
  double c0 = x;
  double c1 = xd;
  double c2 = (3*t-t_togo*td-3*x-2*t_togo*xd)/t_togo/t_togo;
  double c3 = (2*x+t_togo*xd-2*t+td*t_togo)/t_togo/t_togo/t_togo;

  *x_next = c0+c1*dt+c2*dt*dt+c3*dt*dt*dt;
  *xd_next = c1+2*c2*dt+3*c3*dt*dt;
  *xdd_next = 2*c2+6*c3*dt;
  // if(tick<=(int)(axis_time / time_step)*2 && tick >= (int)(axis_time / time_step)-30)
  // printf("%f\n", *x_next);

  return TRUE;
}