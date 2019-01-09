% Load data
noisy_data = load("./noisy.data");
y = noisy_data(:, 1);
x = noisy_data(:, 2);
u = noisy_data(:, 3);
% Get filter parameters
[B,A] = butter(2, 5/(100/2));
% Apply filter
filtered = filter(B, A, y);

figure(1);
hold on;
h1 = plot(x,'b');
h2 = plot(filtered, 'g');
legend([h1,h2], 'x', 'filtered');

figure(2);
subplot(3, 1, 1);
plot(y);
ylabel('y');
subplot(3,1,2);
plot(x);
ylabel('x');
subplot(3,1,3);
plot(filtered);
ylabel('filtered_x');

disp(finddelay(x,filtered));
