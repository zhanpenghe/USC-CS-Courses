import string

split_table = str.maketrans({key: ' '+key+' ' for key in string.punctuation})
remove_table = str.maketrans({key: ' ' for key in [',', '.', '\"', '\'', '-', ';', ':', '$', '#']})


def preprocess(sentence):
    global split_table

    sentence = sentence.translate(split_table)
    return sentence


def load_training_set(path):
    ids = []
    labels = []
    sentences = []
    with open(path, 'r') as f:
        for line in f:
            try:
                infos = preprocess(line).split()

                ids.append(infos[0])

                lbl = list()
                lbl.append(1. if infos[1] == 'True' else -1.)
                lbl.append(1. if infos[2] == 'Pos' else -1.)
                labels.append(lbl)

                sentences.append([s for s in infos[3:]])
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


def count_features(sentences):
    feature_set = set()
    for s in sentences:
        for f in s:
            feature_set.add(f)
    print(len(feature_set))

    
class PerceptronClassfier(object):
    
    def __init__(self):
        super(PerceptronClassfier, self).__init__()
        self.w = [dict(), dict()]
        self.b = [0.0, 0.0]

    def train_on_batch(self, labels, sentences):
        for i in range(len(labels)):
            self.train_on_instance(labels[i], sentences[i])

    def train_on_instance(self, labels, features):

        a = [self.b[0], self.b[1]]

        for i in range(2):
            for f in features:
                if f not in self.w[i]:
                    self.w[i][f] = 0
                a[i] += (self.w[i][f])

        for i in range(2):
            if labels[i] * a[i] <= 0:
                self.b[i] += labels[i]
                for f in features:
                    self.w[i][f] += labels[i]

    def classify(self, sentence):

        a = [self.b[0], self.b[1]]

        for f in sentence:
            for i in range(2):
                w = self.get_weight(i, f)
                a[i] += w
        # print(a)
        return a

    def fit(self, X, y, epochs=10):
        for _ in range(epochs):
            self.train_on_batch(
                sentences=X,
                labels=y,
            )

    def test(self, sentences):
        results = list()
        for s in sentences:
            re = self.classify(s)
            re_str = ['True', 'Pos']
            if re[0] < 0:
                re_str[0] = 'Fake'
            if re[1] < 0:
                re_str[1] = 'Neg'
            results.append(re_str)
            # print(re_str)
        return results

    def get_weight(self, i, f):
        if f not in self.w[i]:
            return 0.
        else:
            return self.w[i][f]

    def save_model(self, file_path='./vanilamodel.txt'):
        with open(file_path, 'w') as f:
            f.write(
                str(self.w)+'\n',
                str(self.b),
            )

    def load_model(self, file_path='./vanilamodel.txt'):
        import ast

        with open(file_path, 'r') as f:

            content = f.read()
            w, b = content.split('\n')
            self.w = ast.literal_eval(w)
            self.b = ast.literal_eval(b)


def main():
    ids, labels, sentences = load_training_set('data/train-labeled.txt')
    count_features(sentences)
    classifier = PerceptronClassfier()
    classifier.fit(X=sentences, y=labels, epochs=30)
    test_ids, test_sentences = load_dev_set('data/dev-text.txt')
    test_key = load_dev_key('./data/dev-key.txt')
    print(len(classifier.w[0]))

    print('\n-----\nTEST RESULT:\n')
    correct_count = 0
    label1_count = 0
    label2_count = 0
    total_count = 0

    results = classifier.test(test_sentences)

    for i in range(len(results)):

        id = test_ids[i]

        total_count += 1
        test_re = results[i]

        key = test_key[id]

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

if __name__ == '__main__':
    main()
