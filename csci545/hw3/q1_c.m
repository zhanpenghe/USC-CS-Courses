data = load('noisy.data');
y = data(:,1);
x = data(:,2);
u = data(:,3);

A = 0.9;
B = 0.5;

C = 1;
R = 1;
Q = 0.01;

K = zeros(1000,1);
P_post = zeros(1000,1);
x_post = zeros(1000,1);

x_pri = 0;
% Set P(0) = 0 is equalvalent to set P_pri(1) = Q
P_pri = A * 1 * A + Q; 

for t = 1:1000
    K(t) = P_pri * C / ( C * P_pri * C + R );
    x_post(t) = x_pri + K(t) * ( y(t) - C * x_pri );
    P_post(t) = ( 1 - K(t) * C ) * P_pri;
    x_pri = A * x_post(t) + B * u(t);
    P_pri = A * P_post(t) * A + Q;
end


figure(3);
subplot(2,1,1);
plot(x);
ylabel('x');
subplot(2,1,2);
plot(x_post);
ylabel('x\_filtered');

figure(4);
plot(K);
title('K');

figure(5);
plot(P_post);
title('P');

figure(6);
hold on;
h1 = plot(x,'b');
h2 = plot(x_post, 'g');
legend([h1,h2], 'x', 'x\_filtered');

disp(finddelay(x,x_post));