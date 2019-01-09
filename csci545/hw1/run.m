x_curr = 0;
x_dot_curr = 0;
x_f = 2;
x_dot_f = 0;
tao = 2;
delta_t = 0.01;

pos = zeros(1, 201);
vel = zeros(1, 201);
acc = zeros(1, 201);
re_vec = zeros(1, 4);
i = 0;

for t = 0.0:0.01:tao
    i = i+1;
    remaining_time = tao - t;
    [x_curr,x_dot_curr,x_dot_dot_curr]=planning(x_curr, x_dot_curr, x_f, x_dot_f, remaining_time, delta_t);
    
    pos(i) = x_curr;
    vel(i) = x_dot_curr;
    acc(i) = x_dot_dot_curr;
    
end

x = 0.0:0.01:tao;

subplot(3, 1, 1)
plot(x, pos)
xlabel("Time")
ylabel("x")

subplot(3, 1, 2)
plot(x, vel)
xlabel("Time")
ylabel("x")

subplot(3, 1, 3)
plot(x, acc)
xlabel("Time")
ylabel("x")
