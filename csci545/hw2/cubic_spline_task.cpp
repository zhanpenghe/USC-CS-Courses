/*============================================================================
==============================================================================
                      
                              cubic_spline_task.c
 
==============================================================================
Remarks:

      sekeleton to create the sample task

============================================================================*/

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


static double      time_step;
static double     *cart;
static SL_Cstate  *ctarget;
static SL_Cstate  *cnext;
static int        *cstatus;
static SL_DJstate *target;
static int         firsttime = TRUE;
static double      movement_time;
static double      tau;

double xbasis;
double zbasis;
double amp;

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
static void init_vars(void);

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
  static int firsttime = TRUE;
  
  if (firsttime) {
    firsttime = FALSE;

    cart    = my_vector(1,n_endeffs*6);
    ctarget = (SL_Cstate *) my_calloc(n_endeffs+1,sizeof(SL_Cstate),MY_STOP);
    cnext   = (SL_Cstate *) my_calloc(n_endeffs+1,sizeof(SL_Cstate),MY_STOP);
    cstatus = my_ivector(1,n_endeffs*6);
    target  = (SL_DJstate *)my_calloc(n_dofs+1,sizeof(SL_DJstate),MY_STOP);


    addTask("Cubic Spline Task", init_cubic_spline_task, 
     run_cubic_spline_task, change_cubic_spline_task);
  }

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
  int    j, i;
  char   string[100];
  double max_range=0;
  int    ans;
  double aux;
  int    flag = FALSE;
  int    iaux;
  
  /* check whether any other task is running */
  if (strcmp(current_task_name,NO_TASK) != 0) {
    printf("Goto task can only be run if no other task is running!\n");
    return FALSE;
  }

  /* initialize some variables */
  init_vars();
  
  /* go to the default state */
  for (i=1; i<=n_dofs; ++i)
    target[i] = joint_default_state[i];

  target[R_SFE].th = -1.5;
  target[R_HR].th  = 1.0;
  target[R_EB].th  = 1.5;

  if (!go_target_wait_ID(target))
    return FALSE;

  time_step = 1./(double)task_servo_rate;
  movement_time = 200.0;
  tau = movement_time;

  /* we move with the right hand */
  cstatus[(RIGHT_HAND-1)*6+_X_] = TRUE;
  cstatus[(RIGHT_HAND-1)*6+_Y_] = TRUE;
  cstatus[(RIGHT_HAND-1)*6+_Z_] = TRUE;

  /* choose as target 15 cm distance in x direction */
  ctarget[RIGHT_HAND].x[_X_] = cart_des_state[RIGHT_HAND].x[_X_] + 0.15;
  ctarget[RIGHT_HAND].x[_Y_] = cart_des_state[RIGHT_HAND].x[_Y_] + 0.1;
  ctarget[RIGHT_HAND].x[_Z_] = cart_des_state[RIGHT_HAND].x[_Z_] + 0.1;


  /* the cnext state is the desired state as seen form this program */
  for (i=1; i<=n_endeffs;++i) 
    cnext[i] = cart_des_state[i];

  //*/
  xbasis = 0.07;
  zbasis = -0.03;
  amp = 0.015;
  /*/
      printf("x z:");
      scanf("%lf%lf", &xbasis, &zbasis);
      printf("A:");
      scanf("%lf",&amp);
  //*/
  cnext[RIGHT_HAND].x[_X_] = xbasis;
  cnext[RIGHT_HAND].x[_Z_] = zbasis;
  go_cart_target_wait(cnext, cstatus, 5);

  /* ready to go */
  ans = 999;
  while (ans == 999) {
    if (!get_int("Enter 1 to start or anthing else to abort ...",ans,&ans))
      return FALSE;
  }
    
  if (ans != 1) 
    return FALSE;
  
  scd();

  return TRUE;
}

static void
init_vars(void) 
{
  if (firsttime) {
    firsttime = FALSE;
    ivec_zero(cstatus);
    vec_zero(cart);
    bzero((char *)&(ctarget[1]),n_endeffs*sizeof(ctarget[1]));
  }
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

  // if(tick<=(int)(axis_time / time_step)*2  && tick >= (int)(axis_time / time_step)-30){
  //   printf("--------%d-------\n", tick);
  // }
  int j, i;
  double sum=0;
  double aux;

  /* has the movement time expired? I intentially run 0.5 sec longer */
  if (tau <= -0.5*0 ) {
    freeze();
    return TRUE; 
  }

  /* progress by min jerk in cartesian space */
  //calculate_min_jerk_next_step(cnext,ctarget,tau,time_step,cnext);

  // current time
  double t = movement_time - tau;
  
  // to determine current target state and the time left to finish the current axis
  double axis_time_left = (double)axis_time - (t - axis_time*((int)(t/axis_time)));

  if(axis_time_left <= time_step){

    printf("[INFO] Catch time of 0! time left: %.2f. Current time: %.2f\n", axis_time_left, t);

    axis_time_left = axis_time;
    axis_id = (axis_id+1)%4;
    
    printf("[INFO] Switch to task: %d, time left updated: %f\n", axis_id, axis_time_left);
  }
  double x_target = xbasis;
  double z_target = zbasis;
  
  switch(axis_id){
    case 0:
      x_target = xbasis - 0.03;
      z_target = zbasis - 0.03;
      break;
    case 1:
      x_target = xbasis + 0.03;
      z_target = zbasis - 0.03;
      break;
    case 2:
      x_target = xbasis + 0.03;
      z_target = zbasis + 0.03;
      break;
    case 3:
      x_target = xbasis - 0.03;
      z_target = zbasis + 0.03;
      break;
    default:
      break;
  }

  tick += 1;

  double x_next=0.0, x_next_d=0.0, x_next_dd=0.0;
  double z_next=0.0, z_next_d=0.0, z_next_dd=0.0;

  cubic_spline_next_step (cnext[RIGHT_HAND].x[_X_], cnext[RIGHT_HAND].xd[_X_], cnext[RIGHT_HAND].xdd[_X_], x_target, 0.0, 0.0,
      axis_time_left, time_step, &x_next, &x_next_d, &x_next_dd);
  cubic_spline_next_step (cnext[RIGHT_HAND].x[_Z_], cnext[RIGHT_HAND].xd[_Z_], cnext[RIGHT_HAND].xdd[_Z_], z_target, 0.0, 0.0,
      axis_time_left, time_step, &z_next, &z_next_d, &z_next_dd);

  // double wx = PI/5.0, wz = wx*2.0;
  // cnext[RIGHT_HAND].x[_X_] = amp * sin(wx*t - PI/2.0) + amp + xbasis;
  // cnext[RIGHT_HAND].x[_Z_] = amp * sin(wz*t) + zbasis;
  // cnext[RIGHT_HAND].xd[_X_] = amp * wx * cos(wx*t - PI/2.0);
  // cnext[RIGHT_HAND].xd[_Z_] = amp * wz * cos(wz*t);

  // if(tick % 100 == 0 && tick <= 5000){
  //   printf("task:%d, time: %.4f\n", axis_id, axis_time_left);
  // }



  cnext[RIGHT_HAND].x[_X_] = x_next;
  cnext[RIGHT_HAND].x[_Z_] = z_next;
  cnext[RIGHT_HAND].xd[_X_] = x_next_d;
  cnext[RIGHT_HAND].xd[_Z_] = z_next_d;
  cnext[RIGHT_HAND].xdd[_X_] = x_next_dd;
  cnext[RIGHT_HAND].xdd[_Z_] = z_next_dd;


  // if(tick <= (int)(axis_time / time_step)*2 && tick >= (int)(axis_time / time_step)-30){
  //   // if(axis_id == 1){
  //   //   printf("-----------------------\n");
  //   // }
  //   // printf("%f\n", time_step);
  //   // printf("%.4f\n", axis_time_left);
  //   // printf("%d, %.4f, %.4f\n", axis_id, t, axis_time_left);
  //   //printf("%.4f, %.4f\n", x_target, z_target);
  //   printf("- %.2f, %.2f\n", x_next, z_next);

  //   printf("--%.2f, %.2f\n", x_target, z_target);
  //   printf("%f\n", axis_time_left);
  // }


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


  // assign desired state
  for (i=1; i<=n_dofs; ++i) {

    joint_des_state[i].thd  = target[i].thd;
    joint_des_state[i].th   = target[i].th;

    // check range of motion violation
    if (joint_des_state[i].th > joint_range[i][MAX_THETA]) {
        //printf("!!max\n");
      joint_des_state[i].th = joint_range[i][MAX_THETA];
      joint_des_state[i].thd = 0.0;
    }
    if (joint_des_state[i].th < joint_range[i][MIN_THETA]) {
        //printf("!!min\n");
      joint_des_state[i].th = joint_range[i][MIN_THETA];
      joint_des_state[i].thd = 0.0;
    }
  }

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

  get_int("This is how to enter an integer variable",ivar,&ivar);
  get_double("This is how to enter a double variable",dvar,&dvar);
  //printf("%d\n", ivar);
  return TRUE;

}


/*!*****************************************************************************
 *******************************************************************************
\note  cubic_spline_next_step
\date  April 2014
   
\remarks 

Given the time to go, the current state is updated to the next state
using min jerk splines

 *******************************************************************************
 Function Parameters: [in]=input,[out]=output

 \param[in]          x,xd,xdd : the current state, vel, acceleration
 \param[in]          t,td,tdd : the target state, vel, acceleration
 \param[in]          t_togo   : time to go until target is reached
 \param[in]          dt       : time increment
 \param[in]          x_next,xd_next,xdd_next : the next state after dt

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

