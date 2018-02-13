

class HMM(object):

    def __init__(self):
        super(HMM, self).__init__()
        self.transition_prob = []
        self.emission_prob = []

    def learn(self, words, tags):

        assert len(words) == len(tags)

        transition_counts = {'START_STATE': {}}
        emission_counts = {}

        for i in range(len(words)):

            observation_seq = words[i]
            tag_seq = tags[i]

            # Starting state->first state
            # First state->first observation

            if tag_seq[0] not in transition_counts['START_STATE']:
                transition_counts['START_STATE'][tag_seq[0]] = 0
            if tag_seq[0] not in emission_counts:
                emission_counts[tag_seq[0]] = {}
            if observation_seq[0] not in emission_counts[tag_seq[0]]:
                emission_counts[tag_seq[0]][observation_seq[0]] = 0

            transition_counts['START_STATE'][tag_seq[0]] = transition_counts['START_STATE'][tag_seq[0]]+1
            emission_counts[tag_seq[0]][observation_seq[0]] = emission_counts[tag_seq[0]][observation_seq[0]]+1

            for j in range(1, len(observation_seq)):

                if tag_seq[j-1] not in transition_counts:
                    transition_counts[tag_seq[j-1]] = {}
                if tag_seq[j] not in transition_counts[tag_seq[j-1]]:
                    transition_counts[tag_seq[j-1]][tag_seq[j]] = 0
                if tag_seq[j] not in emission_counts:
                    emission_counts[tag_seq[j]] = {}
                if observation_seq[j] not in emission_counts[tag_seq[j]]:
                    emission_counts[tag_seq[j]][observation_seq[j]] = 0

                transition_counts[tag_seq[j-1]][tag_seq[j]] = transition_counts[tag_seq[j-1]][tag_seq[j]] + 1
                emission_counts[tag_seq[j]][observation_seq[j]] = emission_counts[tag_seq[j]][observation_seq[j]] + 1


def load_data(path):

    X = []
    y = []

    with open(path, 'r') as file:
        for line in file:

            tokens = line.split(' ')
            words = []
            tags = []

            for tk in tokens:
                start_of_tag = tk.rfind('/')+1
                words.append(tk[0:start_of_tag-1].strip())
                tags.append(tk[start_of_tag:].strip())

            assert len(words) == len(tags)
            X.append(words)
            y.append(tags)

    return X, y


words, tag = load_data('./dataset/en_train_tagged.txt')
print(words[0])
print(tag[0])

hmm = HMM()
hmm.learn(words=words, tags=tag)

