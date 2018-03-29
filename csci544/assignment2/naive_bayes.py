import string, math

table = str.maketrans({key: ' ' for key in string.punctuation})


def preprocess(sentence):
    global table

    return sentence.translate(table)


def load_dataset(path):
    ids = []
    labels = []
    sentences = []
    with open(path, 'r') as f:
        for line in f:
            infos = preprocess(line).split()
            # print(infos)
            ids.append(infos[0])
            labels.append(infos[1:3])
            sentences.append(infos[3:])

    assert len(ids) == len(labels) and len(labels) == len(sentences)

    return (ids, labels, sentences)


def load_dev_set(path):
    sentences = list()
    ids = list()
    with open(path, 'r') as f:
        for line in f:
            infos = preprocess(line).split()
            ids.append(infos[0])
            sentences.append(infos[1:])

    return ids, sentences


class NaiveBayesClassifier(object):

    def __init__(self, sentences, labels):
        super(NaiveBayesClassifier, self).__init__()

        # Training data
        self.sentences = sentences
        self.labels_data = labels
        self.training_data_len = len(sentences)

        # Features
        self.words = None
        self.labels = None
        self.posterior_prob = list()
        self.prior_prob = list()

    def learn(self):

        count_prior, count_posterier = self._count()

        for i in range(len(self.labels)):
            self.prior_prob.append({})
            self.posterior_prob.append({})
            count_sum = sum(count_prior[i].values())
            for lbl in self.labels[i]:
                self.prior_prob[i][lbl] = math.log(count_prior[i][lbl]/count_sum)

            for lbl in self.labels[i]:
                if lbl not in self.posterior_prob[i]:
                    self.posterior_prob[i][lbl] = {}
                count_sum = sum(count_posterier[i][lbl].values())
                for word in self.words:
                    self.posterior_prob[i][lbl][word] = math.log(count_posterier[i][lbl][word]/count_sum, 2.0)
        print(self.prior_prob)
        print(self.posterior_prob)

    def _count(self, cleanup=False, cleanup_count=1):

        count_posteriors = list()
        count_priors = list()

        words_set = set()

        for i in range(len(self.labels_data[0])):
            count_posteriors.append({})
            count_priors.append({})

        for i in range(self.training_data_len):

            labels = self.labels_data[i]

            # Update priors
            for j in range(len(labels)):
                if labels[j] not in count_priors[j]:
                    count_priors[j][labels[j]] = 0
                if labels[j] not in count_posteriors[j]:
                    count_posteriors[j][labels[j]] = {}
                count_priors[j][labels[j]] += 1

            # Update posteriors
            sentence = self.sentences[i]
            for word in sentence:
                word = word.strip().lower()
                words_set.add(word)
                for j in range(len(labels)):
                    if word not in count_posteriors[j][labels[j]]:
                        count_posteriors[j][labels[j]][word] = 0
                    count_posteriors[j][labels[j]][word] += 1

        # cleanup count that is less than cleanup_count, default is 1
        if cleanup:
            pass

        print(count_posteriors)
        print(len(count_posteriors[0]['True'].keys()), len(count_posteriors[1]['Pos'].keys()))
        print(count_priors)

        self.labels = [set(lbl.keys()) for lbl in count_priors]
        self.words = words_set
        print(self.labels)

        for word in words_set:
            for distribution in count_posteriors:
                for lbl in distribution:
                    if word not in distribution[lbl]:
                        distribution[lbl][word] = 0
                    distribution[lbl][word] += 1

        return count_priors, count_posteriors

    def _get_posterior(self, label_id, label, word):
        try:
            return self.posterior_prob[label_id][label][word]
        except Exception as e:
            # print('Unknown word:', word)
            return 1.0

    def classify(self, sentence):

        probs = []

        for i in range(len(self.labels)):
            probs.append({})
            for lbl in self.labels[i]:
                probs[i][lbl] = self.prior_prob[i][lbl]
                for word in sentence:
                    probs[i][lbl] += self._get_posterior(label_id=i, label=lbl, word=word.lower())

        result = []

        for i in range(len(self.labels)):
            result.append(max(probs[i], key=probs[i].get))

        return result

    def save_model(self, file_path='./nbmodel.txt'):
        with open(file_path, 'w') as f:
            f.write((str(self.prior_prob) + '\n' + str(self.posterior_prob)) + '\n' + str(self.labels) + '\n' + str(
                self.words))

    def load_model(self, file_path='./nbmodel.txt'):
        import ast

        with open(file_path, 'r') as f:

            content = f.read()
            prior_prob, posterior_prob, labels, words = content.split('\n')

            self.prior_prob= ast.literal_eval(prior_prob)
            self.posterior_prob = ast.literal_eval(posterior_prob)
            self.labels = ast.literal_eval(labels)
            self.words = ast.literal_eval(words)


data_set = load_dataset('./data/train-labeled.txt')
nb = NaiveBayesClassifier(sentences = data_set[2], labels = data_set[1])
nb.learn()

results = dict()
result_list = list()
ids, test_set = load_dev_set('./data/dev-text.txt')
for i in range(len(test_set)):
    re = nb.classify(test_set[i])
    results[ids[i]] = re
    result_list.append(re)
    print(re)