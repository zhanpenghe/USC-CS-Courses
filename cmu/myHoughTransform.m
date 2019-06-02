function [H, rhoScale, thetaScale] = myHoughTransform(Im, threshold, rhoRes, thetaRes)
%Your implementation here

[row, col] = size(Im);
max_rho = (row^2 + col^2)^0.5;

rho_len = int32(ceil(max_rho/rhoRes) +1);
theta_len = int32(ceil(2*pi/thetaRes + 1));

% to map from index to actual rho and theta values
rhoScale = 0 : rhoRes : rho_len * rhoRes;
rhoScale = double(rhoScale);
thetaScale = 0 : thetaRes : 2*pi;

% accumulator matrix
% will be used for voting
H = zeros(rho_len, theta_len);

for x = 1:row 
    for y = 1:col
        if Im(x,y) >= threshold
            % edge! update the votes
            for j = 1:theta_len
                rho_j = x * cos(thetaScale(j)) + y * sin(thetaScale(j));
                if rho_j > 0
                    i = round(rho_j/rhoRes) + 1;
                    H(i, j) = H(i, j)+1;
                end
            end
        end
    end
end

end
        
        