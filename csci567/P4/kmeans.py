import numpy as np


class KMeans():

    '''
        Class KMeans:
        Attr:
            n_cluster - Number of cluster for kmeans clustering (Int)
            max_iter - maximum updates for kmeans clustering (Int) 
            e - error tolerance (Float)
    '''

    def __init__(self, n_cluster, max_iter=100, e=0.0001):
        self.n_cluster = n_cluster
        self.max_iter = max_iter
        self.e = e

    def fit(self, x):
        '''
            Finds n_cluster in the data x
            params:
                x - N X D numpy array
            returns:
                A tuple
                (centroids a n_cluster X D numpy array, y a size (N,) numpy array where cell i is the ith sample's assigned cluster, number_of_updates an Int)
            Note: Number of iterations is the number of time you update the assignment
        '''
        assert len(x.shape) == 2, "fit function takes 2-D numpy arrays as input"
        np.random.seed(42)
        N, D = x.shape

        # TODO:
        # - comment/remove the exception.
        # - Initialize means by picking self.n_cluster from N data points
        # - Update means and membership until convergence or until you have made self.max_iter updates.
        # - return (means, membership, number_of_updates)

        # DONOT CHANGE CODE ABOVE THIS LINE
        initial_centroid_idx = set()
        while len(initial_centroid_idx) < self.n_cluster:
            choice = np.random.choice(N)
            if choice not in initial_centroid_idx:
                initial_centroid_idx.add(choice)

        initial_centroids = x[list(initial_centroid_idx), :].copy()
        assert initial_centroids.shape == (self.n_cluster, D)
        centroids = initial_centroids

        clusters = np.zeros(N)

        update = 0
        J_previous = 0
        for itr in range(self.max_iter):
            # clusters = [[] for _ in range(self.n_cluster)]
            # Get clusters first
            dists = []
            for c in centroids:
                dist = np.sum(np.square(c - x), axis=1)
                dists.append(dist)
            dists = np.array(dists)
            assert dists.shape == (self.n_cluster, N)
            clusters = np.argmin(dists, axis=0)

            # Calculate J
            dists = []
            for k in range(self.n_cluster):
                # TODO change it to norm
                membership = x[np.where(clusters == k)]
                dist = np.sum(np.square(centroids[k, :] - membership))
                dists.append(dist)
            J = np.sum(dists) / N
            delta_J = np.abs(J - J_previous) 
            J_previous = J

            if delta_J < self.e and itr >= 1:
                break

            # Calculate new centroids
            for k in range(self.n_cluster):
                cluster = x[np.where(clusters == k)].copy()
                # TODO: replace with np.mean
                mean = np.sum(cluster, axis=0) / cluster.shape[0]
                centroids[k, :] = mean
            update += 1

        # centroid_labels = np.zeros(N)
        # for k in range(self.n_cluster):
        #     for d in clusters[k]:
        #         centroid_labels[d] = k

        return centroids, clusters, update
        # DONOT CHANGE CODE BELOW THIS LINE

class KMeansClassifier():

    '''
        Class KMeansClassifier:
        Attr:
            n_cluster - Number of cluster for kmeans clustering (Int)
            max_iter - maximum updates for kmeans clustering (Int) 
            e - error tolerance (Float) 
    '''

    def __init__(self, n_cluster, max_iter=100, e=1e-6):
        self.n_cluster = n_cluster
        self.max_iter = max_iter
        self.e = e

    def fit(self, x, y):
        '''
            Train the classifier
            params:
                x - N X D size  numpy array
                y - (N,) size numpy array of labels
            returns:
                None
            Stores following attributes:
                self.centroids : centroids obtained by kmeans clustering (n_cluster X D numpy array)
                self.centroid_labels : labels of each centroid obtained by 
                    majority voting ((N,) numpy array) 
        '''

        assert len(x.shape) == 2, "x should be a 2-D numpy array"
        assert len(y.shape) == 1, "y should be a 1-D numpy array"
        assert y.shape[0] == x.shape[0], "y and x should have same rows"

        np.random.seed(42)
        N, D = x.shape
        # TODO:
        # - comment/remove the exception.
        # - Implement the classifier
        # - assign means to centroids
        # - assign labels to centroid_labels

        # DONOT CHANGE CODE ABOVE THIS LINE
        kmeans_cluster = KMeans(self.n_cluster, self.max_iter, self.e)
        centroids, cluster, update = kmeans_cluster.fit(x)
        centroid_labels = []
        for k in range(self.n_cluster):
            members = y[np.where(cluster == k)]
            votes = dict()
            for c in members:
                votes[c] = votes.get(c, 0) + 1
            centroid_labels.append(sorted(votes.items(), key=lambda x:(-x[1], x[0]))[0][0])
        centroid_labels = np.array(centroid_labels)

        # DONOT CHANGE CODE BELOW THIS LINE

        self.centroid_labels = centroid_labels
        self.centroids = centroids

        assert self.centroid_labels.shape == (self.n_cluster,), 'centroid_labels should be a numpy array of shape ({},)'.format(
            self.n_cluster)

        assert self.centroids.shape == (self.n_cluster, D), 'centroid should be a numpy array of shape {} X {}'.format(
            self.n_cluster, D)

    def predict(self, x):
        '''
            Predict function

            params:
                x - N X D size  numpy array
            returns:
                predicted labels - numpy array of size (N,)
        '''

        assert len(x.shape) == 2, "x should be a 2-D numpy array"

        np.random.seed(42)
        N, D = x.shape
        # TODO:
        # - comment/remove the exception.
        # - Implement the prediction algorithm
        # - return labels

        # DONOT CHANGE CODE ABOVE THIS LINE

        labels = []
        for d in x:
            dist = np.sum(np.square(d - self.centroids), axis=1)
            labels.append(self.centroid_labels[np.argmin(dist, axis=0)])
        labels = np.array(labels)
        # DONOT CHANGE CODE BELOW THIS LINE
        return labels

