function [img1] = myEdgeFilter(img0, sigma)
%Your implemention
hsize = 2 * ceil(3 * sigma) + 1;
h = fspecial('gaussian', hsize, sigma);
% apply filter
filtered = myImageFilter(img0, h);

% image gradients
sobel = [-0.5 0 0.5];
imgx = myImageFilter(filtered, sobel);
imgy = myImageFilter(filtered, sobel');

% calculate gradient magnitude
magnitude = (imgx .^ 2 + imgx .^ 2) .^ .5;

% gradient direction
[row, col] = size(img0);
direction = zeros(size(img0));
for r = 1 : row
    for c = 1 : col
        grad_x = imgx(r, c);
        grad_y = imgy(r, c);
        deg = atan(grad_y/grad_x);
        if -pi/2 <= deg < -3*pi/8
            direction(r, c) = pi/2;
        elseif -3*pi/8 <= deg < -pi/8
            direction(r, c) = 3*pi/4;
        elseif -pi/8 <= deg < pi/8
            direction(r, c) = 0;
        elseif pi/8 <= deg < 3*pi/8
            direction(r, c) = pi/4;
        elseif 3*pi/8 <= deg <= pi/2
            direction(r, c) = pi/2;
        end
    end
end

% no-max compression
% search along the direction
for r = 2 : row-1
    for c = 2 : col-1
       d = direction(r, c);
       switch d
           case 0
               neg = magnitude(r, c-1);
               pos = magnitude(r, c+1);
           case pi/4
               neg = magnitude(r-1, c+1);
               pos = magnitude(r+1, c-1);
           case pi/2
               neg = magnitude(r-1, c);
               pos = magnitude(r+1, c);
           case 3*pi/4
               neg = magnitude(r-1, c-1);
               pos = magnitude(r+1, c+1);
       end
       
       if magnitude(r, c) < pos || magnitude(r, c) < neg
           img1(r, c) = 0;
       else
           img1(r, c) = magnitude(r, c);
       end
    end
end
end
    
                
        
        
