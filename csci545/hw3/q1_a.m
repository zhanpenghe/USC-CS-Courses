
[B,A] = butter(2, 5/(100/2));
disp("b1, b2, b3:")
disp(B)
disp("a2, a3:")
disp(A([2, 3]))
