import argparse
from naive_bayes import load_dev_set
from naive_bayes import NaiveBayesClassifier as NB


def save_results(path, results, ids, mode=0):

    result_str = ''

    with open(path, 'w') as f:
        for i in range(len(results)):
            id = ids[i]
            re = results[i]
            if mode == 0:
                result_str += (id+' '+re[0]+' '+re[1]+'\n')
            elif mode == 1:
                result_str += (id+' '+re[0][0:4]+' '+re[0][4:]+'\n')

        f.write(result_str)


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument('path', type=str, help='the path to training data')
    args = parser.parse_args()

    test_data_path = args.path
    ids, sentences = load_dev_set(test_data_path)

    nb = NB(sentences=None, labels=None)
    nb.load_model('./nbmodel.txt')

    results = list()

    for s in sentences:
        re = nb.classify(s)
        results.append(re)

    save_results('./nboutput.txt', results, ids)


if __name__ == '__main__':
    main()