

x_curr = 0;
x_dot_curr = 0;
x_f = 2.0;
x_dot_f = 0;

remaining_time = 2.0;
delta_t = 0.01;

c_0 = x_curr;
c_1 = x_dot_curr;
c_2 = (3*x_f-remaining_time*x_dot_f-3*x_curr-2*remaining_time*x_dot_curr)/remaining_time/remaining_time;
c_3 = (2*x_curr+remaining_time*x_dot_curr-2*x_f+remaining_time*x_dot_f)/remaining_time/remaining_time/remaining_time;

pos = zeros(1, 201);
vel = zeros(1, 201);
acc = zeros(1, 201);

i = 0;

for t = 0:0.01:remaining_time
    i = i+1;
    pos(i) = c_0+c_1*t+c_2*t*t+c_3*t*t*t;
    vel(i) = c_1+2*c_2*t+3*c_3*t*t;
    acc(i) = 2*c_2+6*c_3*t;
end

x = 0.0:0.01:remaining_time;

subplot(3, 1, 1)
plot(x, pos)
xlabel("Time")
ylabel("x")

subplot(3, 1, 2)
plot(x, vel)
xlabel("Time")
ylabel("xd")

subplot(3, 1, 3)
plot(x, acc)
xlabel("Time")
ylabel("xdd")
