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

// defines

// local variables
static double      start_time = 0.0;
static SL_DJstate  target[N_DOFS+1];
static double      delta_t = 0.01;
static double      duration = 2.0;
static double      time_to_go;
static int         target_ = 0;
static double      initial_state_L_SAA = -1.0314133014066027*0.95;
static double      initial_state_L_EB = 1.4356102189600017*0.95;
static int         tick = 0;


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
Function Name	: add_cubic_spline_task
Date		: Feb 1999
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
  
  addTask("Cubic Spline Task", init_cubic_spline_task, 
	  run_cubic_spline_task, change_cubic_spline_task);

}    

/*****************************************************************************
******************************************************************************
  Function Name	: init_cubic_spline_task
  Date		: Dec. 1997

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
  
  if (firsttime){
    firsttime = FALSE;
  }

  // prepare going to the default posture
  bzero((char *)&(target[1]),N_DOFS*sizeof(target[1]));
  for (i=1; i<=N_DOFS; i++)
    target[i] = joint_default_state[i];

  // go to the target using inverse dynamics (ID)
  if (!go_target_wait_ID(target)) 
    return FALSE;

  // re-use the variable target for our min-jerk movement: only the right arm moves
  target[L_SFE].th = 0;
  target[L_SAA].th = initial_state_L_SAA;
  target[L_EB].th  = initial_state_L_EB;
  target[L_HR].th  = 0;

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
  time_to_go = duration;

  return TRUE;
}

/*****************************************************************************
******************************************************************************
  Function Name	: run_cubic_spline_task
  Date		: Dec. 1997

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

  double task_time;

  // ******************************************
  // NOTE: all array indices start with 1 in SL
  // ******************************************

  task_time = task_servo_time - start_time;
  //printf("%f\n", task_time);

  /*double past = duration - time_to_go;
  printf("%f\n", past);
  if(past>=2){
    target[R_SFE].th = joint_default_state[R_SFE].th+0.8;
    target[R_SAA].th = joint_default_state[R_SAA].th-0.5;
    target[R_EB].th  = joint_default_state[R_SAA].th+0.5;
  }

  double local_time = 4;
  if(past>4){
    //
  }
  */
  // compute the update for the desired states
  for (i=1; i<=N_DOFS; ++i) {
    cubic_spline_next_step(joint_des_state[i].th,
			   joint_des_state[i].thd,
			   joint_des_state[i].thdd,
			   target[i].th,
			   target[i].thd,
			   target[i].thdd,
			   time_to_go,
			   delta_t,
			   &(joint_des_state[i].th),
			   &(joint_des_state[i].thd),
			   &(joint_des_state[i].thdd));
  }

  // compute inverse dynamics torques
  SL_InvDynNE(joint_state,joint_des_state,endeff,&base_state,&base_orient);
  
  // decrement time to go
  time_to_go -= delta_t;
    if (time_to_go <= 0){
      
      time_to_go = duration;
      tick += 1;
      target_ = tick % 4;

      switch (target_){
          case 0:
              target[L_SFE].th = 0;
              target[L_SAA].th = initial_state_L_SAA;
              target[L_EB].th  = initial_state_L_EB;
              target[L_HR].th  = 0;
              break;
          case 1:
              target[L_SFE].th = 0;
              target[L_SAA].th =
                      -1.05178561237;
              target[L_EB].th  =
                      1.31974593306;
              target[L_HR].th  = 0;
              break;
          case 2:
              target[L_SFE].th = 0;
              target[L_SAA].th =
                      -0.834234553;
              target[L_EB].th  =
                      1.02658967558;
              target[L_HR].th  = 0;
              break;
          case 3:
              target[L_SFE].th = 0;
              target[L_SAA].th =
                      -0.77350197248;
              target[L_EB].th  =
                      1.07528279126;
              target[L_HR].th  = 0;
              break;
      }

      if (tick > 20){
        freeze();
      }
  }

  return TRUE;
}

/*****************************************************************************
******************************************************************************
  Function Name	: change_cubic_spline_task
  Date		: Dec. 1997

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

  return TRUE;
}

