import argparse
from hmm import HMM, load_tagged_data


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument('path', type=str, help='the path to training data')
    args = parser.parse_args()

    training_data_path = args.path
    train_words, train_tags = load_tagged_data(training_data_path)

    hmm = HMM()
    hmm.learn(words=train_words, tags=train_tags)

    hmm.save_model()


if __name__ == '__main__':
    main()