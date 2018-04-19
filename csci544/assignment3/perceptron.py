import string

split_table = str.maketrans({key: ' ' for key in string.punctuation})
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

                lbl = []
                lbl.append(1. if infos[1] == 'True' else -1.)
                lbl.append(1. if infos[2] == 'Pos' else -1.)
                labels.append(lbl)

                sentences.append([s.lower() for s in infos[3:]])
            except Exception:
                continue

    assert len(ids) == len(labels) and len(labels) == len(sentences)

    return (ids, labels, sentences)

    
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

    def classify(self, sentences):

        a = [self.b[0], self.b[1]]

        for feature in sentences:
            f = feature.lower()
            for i in range(2):
                w = self.get_weight(i, f)
                a += w

    def get_weight(self, i, f):
        if f not in self.w[i]:
            return 0.
        else:
            return self.w[i][f]


def main():
    ids, labels, sentences = load_training_set('data/train-labeled.txt')
    print(labels)
    clasifier = PerceptronClassfier()
    clasifier.train_on_batch(labels[:], sentences[:])
    print(clasifier.w)
    print(clasifier.b)


if __name__ == '__main__':
    main()
