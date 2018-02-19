

# not gonna use numpy for the assignment -_-||
def argmax(lst):
    f = lambda x: lst[x]
    return max(range(len(lst)), key=f)


class HMM(object):

    def __init__(self, smoothing=1e-4, start_state='START_STATE', end_state='END_STATE'):
        super(HMM, self).__init__()
        self.transition_prob = {}
        self.emission_prob = {}
        self.smoothing = smoothing
        self.training_words = None
        self.training_tags = None

        self.tags = []
        self.observations = []
        self.start_state = start_state
        self.end_state = end_state

    def _count_words_and_tags(self, words, tags, percentage=1):
        assert len(words) == len(tags)

        # Save the data for future use for evaluation of training accuracy
        self.training_words = words
        self.training_tags = tags

        transition_counts = {self.start_state: {}}
        emission_counts = {}

        tags_set = {}
        observations_set = {}

        # 0.3 <= length <= 1.0, scale to 1 or 0.3 if out of bound
        learned_length = int(len(words) * max([min([percentage, 1.0]), 0.3]))

        print('Start learning from data of length: ' + str(learned_length))

        for i in range(len(words)):

            observation_seq = words[i]
            tag_seq = tags[i]

            # Starting state->first state
            # First state->first observation

            if tag_seq[0] not in transition_counts[self.start_state]:
                transition_counts[self.start_state][tag_seq[0]] = 0
            if tag_seq[0] not in emission_counts:
                emission_counts[tag_seq[0]] = {}
            if observation_seq[0] not in emission_counts[tag_seq[0]]:
                emission_counts[tag_seq[0]][observation_seq[0]] = 0

            transition_counts[self.start_state][tag_seq[0]] = transition_counts[self.start_state][tag_seq[0]] + 1
            emission_counts[tag_seq[0]][observation_seq[0]] = emission_counts[tag_seq[0]][observation_seq[0]] + 1

            if tag_seq[0] not in tags_set:
                tags_set[tag_seq[0]] = None
            if observation_seq[0] not in observations_set:
                observations_set[observation_seq[0]] = None

            for j in range(1, len(observation_seq)):

                if tag_seq[j - 1] not in transition_counts:
                    transition_counts[tag_seq[j - 1]] = {}
                if tag_seq[j] not in transition_counts[tag_seq[j - 1]]:
                    transition_counts[tag_seq[j - 1]][tag_seq[j]] = 0
                if tag_seq[j] not in emission_counts:
                    emission_counts[tag_seq[j]] = {}
                if observation_seq[j] not in emission_counts[tag_seq[j]]:
                    emission_counts[tag_seq[j]][observation_seq[j]] = 0

                transition_counts[tag_seq[j - 1]][tag_seq[j]] = transition_counts[tag_seq[j - 1]][tag_seq[j]] + 1
                emission_counts[tag_seq[j]][observation_seq[j]] = emission_counts[tag_seq[j]][observation_seq[j]] + 1

                if tag_seq[j] not in tags_set:
                    tags_set[tag_seq[j]] = None
                if observation_seq[j] not in observations_set:
                    observations_set[observation_seq[j]] = None

            # Last tag->end tag
            last_tag = tag_seq[len(tag_seq) - 1]
            if last_tag not in transition_counts:
                transition_counts[last_tag] = {}
            if self.end_state not in transition_counts[last_tag]:
                transition_counts[last_tag][self.end_state] = 0
            transition_counts[last_tag][self.end_state] = transition_counts[last_tag][self.end_state] + 1

        self.tags = set(tags_set.keys())
        self.tags.add(self.start_state)
        self.tags.add(self.end_state)
        self.observations = set(observations_set.keys())

        # Add one for transition probability
        for prev_tag in self.tags:
            for curr_tag in self.tags:
                if prev_tag not in transition_counts:
                    transition_counts[prev_tag] = {}
                if curr_tag not in transition_counts[prev_tag]:
                    transition_counts[prev_tag][curr_tag] = 0
                transition_counts[prev_tag][curr_tag] = transition_counts[prev_tag][curr_tag] + 1

        self.tags.remove(self.start_state)
        self.tags.remove(self.end_state)

        return transition_counts, emission_counts

    def learn(self, words, tags, percentage=1):
        """
        :param words: A list of sentences(also list).
        :param tags: A list of sequences of tags corrisponding to the words
        :param percentage: The portion of corpus that the model learns from.
        """
        transition_counts, emission_counts = self._count_words_and_tags(words, tags, percentage)

        # Compute the posterior probabilities P(curr_state|prev_state) and P(observation|curr_state)
        for state in transition_counts:

            if state not in self.transition_prob:
                self.transition_prob[state] = {}

            next_state_counts = transition_counts[state]
            total_counts = sum(next_state_counts.values())
            for next_state in next_state_counts:
                self.transition_prob[state][next_state] = next_state_counts[next_state]/total_counts

            # Check whether the probabilities satisfy markov chain property
            assert (abs(sum(self.transition_prob[state].values())-1.0) <= 1e-5)

        # Compute the posterior probabilities P(curr_state|prev_state) and P(observation|curr_state)
        for state in emission_counts:

            if state not in self.emission_prob:
                self.emission_prob[state] = {}

            observation_counts = emission_counts[state]
            total_counts = sum(observation_counts.values())
            for observation in observation_counts:
                self.emission_prob[state][observation] = observation_counts[observation] / total_counts

            # Check whether the probabilities satisfy markov chain property
            assert (abs(sum(self.emission_prob[state].values()) - 1.0) <= 1e-5)

    def save_model(self, file_path='./hmmmodel.txt'):
        with open(file_path, 'w') as f:
            f.write((str(self.transition_prob)+'\n'+str(self.emission_prob))+'\n'+str(self.tags)+'\n'+str(self.observations))

    def load_model(self, file_path='./hmmmodel.txt'):
        import ast

        with open(file_path, 'r') as f:

            content = f.read()
            transition_str, emission_str, tags, obs = content.split('\n')

            self.transition_prob = ast.literal_eval(transition_str)
            self.emission_prob = ast.literal_eval(emission_str)
            self.tags = ast.literal_eval(tags)
            self.observations = ast.literal_eval(obs)

        '''
        with open(transition_prob_path, 'r') as f:
            prob_str = f.read()

            self.transition_prob = ast.literal_eval(prob_str)
            # Check the probabilities
            for state in self.transition_prob:
                # print(sum(self.transition_prob[state].values()))
                assert (abs(sum(self.transition_prob[state].values()) - 1.0) <= 1e-5)

        with open(emission_prob_path, 'r') as f:
            prob_str = f.read()

            self.emission_prob = ast.literal_eval(prob_str)
            # Check the probabilities
            for state in self.emission_prob:
                # print(sum(self.emission_prob[state].values()))
                assert (abs(sum(self.emission_prob[state].values()) - 1.0) <= 1e-5)
        '''

    def decode(self, sentence):

        """
        Viterbi Algorithm for part of speech tagging
        :param:
        :return:
        """

        assert type(sentence) is list
        assert len(sentence) > 0

        #print(self.tags)
        nb_tags = len(self.tags)
        tag_list = list(self.tags)

        back_pointers = [[] for _ in range(nb_tags)]
        path_probs = [self.get_transition_prob(self.start_state, tag_list[i])*self.get_emission_prob(tag_list[i], sentence[0]) for i in range(nb_tags)]  # A list of list of tuples that contains probabilities and backpointers

        #print(path_probs)
        for s in range(1, len(sentence)):

            #print('\n---\nWorking on word', sentence[s])
            temp_probs = path_probs.copy()
            for i in range(nb_tags):

                tag = tag_list[i]
                max_prob = 0
                max_index = 0

                for j in range(nb_tags):
                    #print('\nComputed:', temp_probs[j], '*', self.get_transition_prob(self.tags[j], tag), '*',
                    #      self.get_emission_prob(tag, sentence[s]))
                    #print(self.tags[j],'->',self.tags[i],'->',sentence[s])

                    prob_j_i = temp_probs[j] \
                                * self.get_transition_prob(tag_list[j], tag) \
                                * self.get_emission_prob(tag, sentence[s])

                    if prob_j_i >= max_prob:
                        max_prob = prob_j_i
                        max_index = j
                    #print('Result:', prob_j_i)
                    #print('Current max_prob:', max_prob, 'max_index', max_index)
                    #print('Current state:', path_probs, back_pointers)

                back_pointers[i].append(max_index)
                path_probs[i] = max_prob
                #print('Current state:', path_probs, back_pointers)

        #print(path_probs)
        #print(back_pointers)

        # End state transition probability
        for i in range(len(path_probs)):
            path_probs[i] = path_probs[i]*self.get_transition_prob(tag_list[i], self.end_state)

        # Backtracking to get the most possible tag sequence
        max_index = argmax(path_probs)
        path = [max_index]

        for i in reversed(range(len(sentence)-1)):
            bp = back_pointers[path[0]][i]
            path.insert(0, bp)
        path = self.get_path_from_indexes(path, tag_list)
        return path

    def get_path_from_indexes(self, path_indexes, tag_list):
        return [tag_list[i] for i in path_indexes]

    def get_transition_prob(self, prev_tag, tag):
        try:
            return self.transition_prob[prev_tag][tag]
        except Exception as e:
            print('bugs..')
            return self.smoothing

    def get_emission_prob(self, tag, observation):
        try:
            if observation not in self.observations:
                return 1
            if observation not in self.emission_prob[tag]:
                return 0
            return self.emission_prob[tag][observation]
        except Exception as e:
            print(e)
            return self.smoothing


class SecondOrderHMM(object):

    def __init__(self, smoothing=1e-4, start_state='START_STATE', end_state='END_STATE'):
        super(HMM, self).__init__()
        self.transition_prob = {}
        self.emission_prob = {}
        self.smoothing = smoothing
        self.training_words = None
        self.training_tags = None

        self.tags = []
        self.observations = []
        self.start_state = start_state
        self.end_state = end_state

    def _count_words_and_tags(self, words, tags):
        pass

    def learn(self):
        pass


def load_raw_data(path):

    X = []

    with open(path, 'r') as file:
        for line in file:
            tokens = line.split(' ')
            words = [w.strip() for w in tokens]
            X.append(words)
    return X


def load_tagged_data(path):

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


def save_result(words, tags, path='./hmmoutput.txt'):
    result_str = ''
    for i in range(len(words)):
        for j in range(len(words[i])):
            result_str+=words[i][j]
            result_str+='/'
            result_str+=tags[i][j]
            if j < len(words[i])-1:
                result_str+=' '
        result_str+='\n'

    with open(path, 'w') as f:
        f.write(result_str)


def test():
    train_words, train_tags = load_tagged_data('./dataset/en_train_tagged.txt')
    dev_words, dev_tags = load_tagged_data('./dataset/en_dev_tagged.txt')
    hmm = HMM()
    hmm.learn(words=train_words, tags=train_tags)
    #print('Training finished.')
    #print(hmm.get_path_from_indexes(hmm.decode(dev_words[1])))

    results = []

    print('Start decoding', len(dev_words), 'sentences')
    for sentence in dev_words:
        tags = hmm.decode(sentence)
        results.append(tags)

    print('Done.\nStart calculate accuracy...')

    count = 0
    correct_tags = 0
    for i in range(len(results)):
        tag_seq = results[i]
        for j in range(len(tag_seq)):
            count += 1
            if results[i][j] == dev_tags[i][j]:
                correct_tags += 1

    print(count, correct_tags, correct_tags/count)


    hmm.save_model()


def test2():

    train_words = [
        ['o_1', 'o_2', 'o_3', 'o_1'],
        ['o_2', 'o_3', 'o_2'],
        ['o_3', 'o_2', 'o_1'],
        ['o_1', 'o_1', 'o_3']
    ]
    train_tags = [
        ['tag_a', 'tag_b', 'tag_c', 'tag_a'],
        ['tag_a', 'tag_c', 'tag_a'],
        ['tag_b', 'tag_c', 'tag_b'],
        ['tag_c', 'tag_a', 'tag_a']
    ]

    hmm = HMM()
    hmm.learn(train_words, train_tags)

    print(hmm.transition_prob)
    print(hmm.emission_prob)

    test_sentence = ['o_1', 'o_2', 'o_3']
    print(hmm.get_path_from_indexes(hmm.decode(test_sentence)))


if __name__ == '__main__':
    test()
