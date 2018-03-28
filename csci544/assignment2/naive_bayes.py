

def load_dataset(path):
    ids = []
    labels = []
    sentences = []
    with open(path, 'r') as f:
        for line in f:
            infos = line.split(' ')
            # print(infos)
            ids.append(infos[0])
            labels.append(infos[1:3])
            sentences.append(infos[3:])

    assert len(ids) == len(labels) and len(labels) == len(sentences)

    return (ids, labels, sentences)


class NaiveBayesClassifier(object):

    def __init__(self, sentences, labels):
        super(NaiveBayesClassifier, self).__init__()

        self.sentences = sentences
        self.labels = labels
        self.training_data_len = len(sentences)
        self._init_features()

    def _init_features(self):

        count_posteriors = []
        count_priors = []
        for i in range(len(self.labels[0])):
            count_posteriors.append({})
            count_priors.append({})

        for i in range(self.training_data_len):

            labels = self.labels[i]

            # Update priors
            for j in range(len(labels)):
                if labels[j] not in count_priors[j]:
                    count_priors[j][labels[j]] = 0
                count_priors[j][labels[j]] += 1

            # Update posteriors
            sentence = self.sentences[i]
            for word in sentence:
                for j in range(len(labels)):
                    if word not in count_posteriors[j]:
                        count_posteriors[j][word] = 0
                    count_posteriors[j][word] += 1

        print(count_posteriors)
        print(count_priors)


data_set = load_dataset('./data/train-labeled.txt')
nb = NaiveBayesClassifier(sentences = data_set[2], labels = data_set[1])

