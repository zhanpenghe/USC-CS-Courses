alpha_z=25;
beta_z=6;
alpha_c=8;
y0=0;
x0=1;
z0=0;
N=10;
g=1;
c=[1.0000 0.6294 0.3962 0.2494 0.1569 0.0988 0.0622 0.0391 0.0246 0.0155];
sigma_2 = [ 41.6667 16.3934 6.5359 2.5840 1.0235 0.4054 0.1606 0.0636 0.0252 0.0252]/1000;
w = [ 0 0 0 0 0 0 0 0 0 0];

dt = 0.001;
ticks= 0:dt:1;

basis_function=zeros(N,size(ticks,2));
yd = zeros(1001, 1);
ydd = zeros(1001, 1);
x= zeros(1001, 1);
y= zeros(1001, 1);
z= zeros(1001, 1);
target_f = zeros( size(ticks,2), 1 );

y(1) = y0;
yd(1) = yd0;
x(1) = x0;

% Calculate x for all ticks
for j = 2:1001
    dx = - alpha_x * x(j-1);
    x(j) = x(j-1) + dx * dt;
end

for j = 2:1001
    for i = 1:N
        basis_function(i, j) = exp(-(x(j)-c(i))^2/(2*sigma_2(i)));
    end
    basis_sum = sum(basis_function(:, j));
    f =dot(basis_function(:, j), w) / basis_sum * x(j) * (g-y0);
    yd(j) = yd(j-1) + dt*ydd(j-1);
    y(j) = y(j-1) + dt*yd(j-1);
    ydd(j) = alpha_z*(beta_z*(g-y(j))-yd(j))+f;
end

figure(1);
hold on;
plot(ticks, y);
title('y');

figure(2);
hold on;
plot(ticks, yd);
title('yd');

figure(3);
hold on;
plot(ticks, ydd);
title('ydd');

figure(4);
plot(ticks, x);
title('x');

figure(5); 
hold on;
for i = 1:N
    plot(ticks, basis_function(i,:) );
    legendInfo{i} = ['psi' num2str(i)];
end
title('basis functions');
legend(legendInfo);
