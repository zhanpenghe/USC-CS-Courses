function [img1] = myImageFilter(img0, h)

% compute padding size
[h_height, h_width] = size(h);
pad_y = floor(h_height / 2);
pad_x = floor(h_width / 2);

% pad the image with 0's
padded_im = padarray(img0, [pad_y, pad_x], 'both');

% output image
img1 = zeros(size(img0));
[im_height, im_width] = size(img0);
h = flip(flip(h, 2));

% apply convolution
for r = pad_y+1 : pad_y+im_height
    for c = pad_x+1 : pad_x+im_width
        window = padded_im(r-pad_y: r+pad_y, c-pad_x: c+pad_x);
        filtered = double(window) .* h;
        val = sum(sum(filtered));
        img1(r-pad_y, c-pad_x) = val;
    end
end

end
