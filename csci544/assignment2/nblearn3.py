import argparse
from naive_bayes import load_training_set
from naive_bayes import NaiveBayesClassifier as NB


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument('path', type=str, help='the path to training data')
    args = parser.parse_args()

    training_data_path = args.path

    _, labels, sentences = load_training_set(path=training_data_path)
    nb = NB(sentences=sentences, labels=labels)
    nb.learn()
    nb.save_model('./nbmodel.txt')


if __name__ == '__main__':
    main()