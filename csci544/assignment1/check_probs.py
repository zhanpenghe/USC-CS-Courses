import ast


def load_probs(path):
    with open(path, 'r') as file:
        content = file.read()
        transition_str, emission_str = content.split('\n')
        transition = ast.literal_eval(transition_str)
        emission = ast.literal_eval(emission_str)

        return transition, emission


def compare_dicts(dict1, dict2, info=''):

    print(info)

    for key in dict1.keys():
        try:
            probs = dict1[key]
            probs2 = dict2[key]
            for k in probs.keys():
                if probs[k] != probs2[k]:
                    return False
        except Exception as e:
            print(e)
            print(key)
            return False
    return True


def compare_hmm_models(file1_path, file2_path):

    transition1, emission1 = load_probs(file1_path)
    transition2, emission2 = load_probs(file2_path)

    transition_result = compare_dicts(transition1, transition2, info='tran1->tran2') and compare_dicts(transition2, transition1, info='tran2->tran1')
    emission_result = compare_dicts(emission1, emission2, info='em1->em2') and compare_dicts(emission2, emission1, info='em2->em1')
    print(transition_result, emission_result)

if __name__ == '__main__':
    compare_dicts('hmmmodel.txt', 'hmmmodel2.txt')