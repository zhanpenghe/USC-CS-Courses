import argparse
from hmm import HMM, load_raw_data, save_result


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument('path', type=str, help='the path to testing data')
    args = parser.parse_args()

    test__data_path = args.path
    test_words = load_raw_data(test__data_path)

    hmm = HMM()
    hmm.load_model('./hmmmodel.txt')

    tag_result = []

    for sentence in test_words:
        re = hmm.decode(sentence)
        tag_result.append(re)

    save_result(test_words, tag_result)


if __name__ == '__main__':
    main()