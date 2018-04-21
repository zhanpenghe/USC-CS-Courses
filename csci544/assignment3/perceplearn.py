import argparse
from perceptron import load_training_set
from perceptron import PerceptronClassfier as PC


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument('path', type=str, help='the path to training data')
    args = parser.parse_args()

    training_data_path = args.path

    _, labels, sentences = load_training_set(path=training_data_path)
    classifier = PC()
    classifier.fit(X=sentences, y=labels, epochs=30)

    classifier.save_model()

if __name__ == '__main__':
    main()
