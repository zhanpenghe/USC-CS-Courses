function [x_t, x_dot_t, x_dot_dot_t] = planning(x_curr, x_dot_curr, x_f, x_dot_f, remaining_time, delta_t)

c_0 = x_curr;
c_1 = x_dot_curr;
c_2 = (3*x_f-remaining_time*x_dot_f-3*x_curr-2*remaining_time*x_dot_curr)/remaining_time/remaining_time;
c_3 = (2*x_curr+remaining_time*x_dot_curr-2*x_f+remaining_time*x_dot_f)/remaining_time/remaining_time/remaining_time;

t = delta_t;

x_t = c_0+c_1*t+c_2*t*t+c_3*t*t*t;
x_dot_t = c_1+2*c_2*t+3*c_3*t*t;
x_dot_dot_t = 2*c_2+6*c_3*t;

end






