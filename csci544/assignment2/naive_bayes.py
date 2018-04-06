import string, math

split_table = str.maketrans({key: ' '+key+' ' for key in string.punctuation})
remove_table = str.maketrans({key: ' ' for key in [',', '.', '\"', '\'', '-', ';', ':', '$', '#']})

def preprocess(sentence):

    global split_table, remove_table

    sentence = sentence.translate(split_table)
    return sentence.translate(remove_table)


def load_training_set(path, mode=0):
    ids = []
    labels = []
    sentences = []
    with open(path, 'r') as f:
        for line in f:
            try:
                infos = preprocess(line).split()

                ids.append(infos[0])
                if mode == 0:
                    labels.append(infos[1:3])
                elif mode == 1:
                    labels.append([infos[1]+infos[2]])
                sentences.append(infos[3:])
            except Exception:
                continue

    assert len(ids) == len(labels) and len(labels) == len(sentences)

    return (ids, labels, sentences)


def load_dev_set(path):
    sentences = list()
    ids = list()
    with open(path, 'r') as f:
        for line in f:
            try:
                infos = preprocess(line).split()
                ids.append(infos[0])
                sentences.append(infos[1:])
            except Exception:
                continue

    return ids, sentences


def load_dev_key(path):

    keys = dict()
    with open(path, 'r') as f:
        for line in f:
            infos = line.split()
            keys[infos[0]] = infos[1:]

    return keys


class NaiveBayesClassifier(object):

    def __init__(self, sentences=None, labels=None):
        super(NaiveBayesClassifier, self).__init__()

        # Training data
        self.sentences = sentences
        self.labels_data = labels
        if sentences is not None:
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
                self.prior_prob[i][lbl] = math.log(count_prior[i][lbl]/count_sum, math.e)

            for lbl in self.labels[i]:
                if lbl not in self.posterior_prob[i]:
                    self.posterior_prob[i][lbl] = {}

                count_sum = sum(count_posterier[i][lbl].values())
                for word in self.words:
                    self.posterior_prob[i][lbl][word] = math.log(count_posterier[i][lbl][word]/count_sum, math.e)
        # print(self.prior_prob)
        # print(self.posterior_prob)

    def _count(self, cleanup=False, cleanup_count=1):

        count_posteriors = list()
        count_priors = list()

        words_set = set()

        for i in range(len(self.labels_data[0])):
            count_posteriors.append({})
            count_priors.append({})

        for i in range(self.training_data_len):

            labels = self.labels_data[i]
            # print(labels)
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
                try:
                    word = word.strip().lower()
                    words_set.add(word)
                    for j in range(len(labels)):
                        if word not in count_posteriors[j][labels[j]]:
                            count_posteriors[j][labels[j]][word] = 0
                        count_posteriors[j][labels[j]][word] += 1
                except Exception:
                    continue

        # cleanup count that is less than cleanup_count, default is 1
        # if cleanup:
        #     pass

        # print(count_posteriors)
        # print(len(count_posteriors[0]['True'].keys()), len(count_posteriors[1]['Pos'].keys()))
        # print(count_priors)

        self.labels = [set(lbl.keys()) for lbl in count_priors]
        self.words = words_set
        # print(self.labels)

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
            return 0.0

    def _get_prior(self, label_id, label):
        try:
            return self.prior_prob[label_id][label]
        except Exception:
            return float('-inf')

    def classify(self, sentence):

        probs = []

        for i in range(len(self.labels)):
            probs.append({})
            for lbl in self.labels[i]:
                probs[i][lbl] = self._get_prior(i, lbl)
                for word in sentence:
                    probs[i][lbl] += self._get_posterior(label_id=i, label=lbl, word=word.strip().lower())

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


def calc_precision_recall(test_result, true_label, pos_tag, i):

    true_pos_count = 0
    true_neg_count = 0
    false_pos_count = 0
    false_neg_count = 0

    for k in test_result:

        test_re = test_result[k][i]
        lbl = true_label[k][i]
        if lbl == pos_tag:
            if test_re == lbl:
                true_pos_count += 1
            else:
                false_pos_count += 1
        else:
            if test_re == lbl:
                false_neg_count += 1
            else:
                true_neg_count += 1

    # print(true_pos_count, true_neg_count, false_pos_count, false_neg_count)

    precision = true_pos_count / (true_pos_count+false_pos_count)
    recall = true_pos_count / (true_pos_count + false_neg_count)
    print(precision, recall)

    return precision, recall


def main():

    mode = 0

    data_set = load_training_set('./data/train-labeled.txt', mode=mode)
    nb = NaiveBayesClassifier(sentences=data_set[2], labels=data_set[1])
    nb.learn()

    results = dict()
    result_list = list()
    ids, test_set = load_dev_set('./data/dev-text.txt')
    for i in range(len(test_set)):
        re = nb.classify(test_set[i])
        results[ids[i]] = re
        result_list.append(re)

    keys = load_dev_key('./data/dev-key.txt')
    # print(keys)

    print('\n-----\nTEST RESULT:\n')
    correct_count = 0
    label1_count = 0
    label2_count = 0
    total_count = 0

    for k in results:

        total_count += 1
        if mode == 0:
            test_re = results[k]
        if mode == 1:
            test_re = [results[k][0][0:4], results[k][0][4:]]
        key = keys[k]

        if test_re[0] == key[0] and test_re[1] == key[1]:
            correct_count += 1
        if test_re[0] == key[0] and key[0]:
            label1_count += 1
        if test_re[1] == key[1]:
            label2_count += 1


    print("Total count:\t"+str(total_count))
    print('Correct count:\t'+str(correct_count)+'(%.4f)'%(correct_count/total_count))
    print('Label #1 correct:\t'+str(label1_count)+'(%.4f)'%(label1_count/total_count))
    print('Label #2 correct:\t' + str(label2_count) + '(%.4f)' % (label2_count / total_count))

    # calc_precision_recall(results, keys, '', 1)


if __name__ == '__main__':
    main()