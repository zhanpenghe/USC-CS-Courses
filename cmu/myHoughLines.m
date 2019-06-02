function [rhos, thetas] = myHoughLines(H, nLines)

%Your implemention here
mask = imregionalmax(H);
masked = H.* mask;

[~, sortedInds] = sort(masked(:), 'descend');
topN = sortedInds(1:nLines);
[rhos, thetas] = ind2sub(size(masked), topN);

end
        