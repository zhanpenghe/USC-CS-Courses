import numpy as np
from typing import List
from classifier import Classifier

class DecisionTree(Classifier):
	def __init__(self):
		self.clf_name = "DecisionTree"
		self.root_node = None

	def train(self, features: List[List[float]], labels: List[int]):
		# init.
		assert(len(features) > 0)
		self.feautre_dim = len(features[0])
		num_cls = np.max(labels)+1

		# build the tree
		self.root_node = TreeNode(features, labels, num_cls)
		if self.root_node.splittable:
			self.root_node.split()

		return
		
	def predict(self, features: List[List[float]]) -> List[int]:
		y_pred = []
		for feature in features:
			y_pred.append(self.root_node.predict(feature))
		return y_pred

	def print_tree(self, node=None, name='node 0', indent=''):
		if node is None:
			node = self.root_node
		print(name + '{')
		
		string = ''
		for idx_cls in range(node.num_cls):
			string += str(node.labels.count(idx_cls)) + ' '
		print(indent + ' num of sample / cls: ' + string)

		if node.splittable:
			print(indent + '  split by dim {:d}'.format(node.dim_split))
			for idx_child, child in enumerate(node.children):
				self.print_tree(node=child, name= '  '+name+'/'+str(idx_child), indent=indent+'  ')
		else:
			print(indent + '  cls', node.cls_max)
		print(indent+'}')


class TreeNode(object):
	def __init__(self, features: List[List[float]], labels: List[int], num_cls: int):
		self.features = features
		self.labels = labels
		self.children = []
		self.num_cls = num_cls

		count_max = 0
		for label in np.unique(labels):
			if self.labels.count(label) > count_max:
				count_max = labels.count(label)
				self.cls_max = label # majority of current node

		if len(np.unique(labels)) < 2:
			self.splittable = False
		else:
			self.splittable = True

		self.dim_split = None # the index of the feature to be split

		self.feature_uniq_split = None # the possible unique values of the feature to be split


	def split(self):
		def conditional_entropy(branches: List[List[int]]) -> float:
			'''
			branches: C x B array, 
					  C is the number of classes,
					  B is the number of branches
					  it stores the number of 
					  corresponding training samples 
					  e.g.
								  ○ ○ ○ ○
								  ● ● ● ●
								┏━━━━┻━━━━┓
							   ○ ○       ○ ○
							   ● ● ● ●
							   
					  branches = [[2,2], [4,0]]
			'''
			########################################################
			# TODO: compute the conditional entropy
			########################################################
			C = len(branches)
			B = len(branches[0])
			branches_mat = np.array(branches).copy()
			# total_counts = np.sum(np.sum(branches)) # TODO CHECK
			# class_counts = np.sum(np.sum(branches), axis=1) # TODO CHECK
			# class_probs = class_counts / total_counts
			
			counts = np.sum(branches_mat, axis=0)
			total_counts = np.sum(counts)
			# counts shape = (B,)
			# branches_mat shape = (C, B)
			# wanted shape = (C, B)
			probs = branches_mat / counts
			cond_ent = 0
			for j in range(probs.shape[1]):
				sum_ent = 0
				for i in range(probs.shape[0]):
					if probs[i, j] == 0:
						continue
					sum_ent += (probs[i, j] * np.log(probs[i, j]))
				cond_ent += (-sum_ent * counts[j] / total_counts)

			return cond_ent
		if len(self.features[0]) == 0:
			self.splittable = False
			return 
		entropies = list()

		for idx_dim in range(len(self.features[0])):
		############################################################
		# TODO: compare each split using conditional entropy
		#       find the best split
		############################################################
			att_values = []
			classes = []
			for i in range(len(self.features)):
				att_values.append(self.features[i][idx_dim])
				classes.append(self.labels[i])

			possible_attributes = np.unique(att_values)
			possible_labels = np.unique(self.labels)
			branches = [[0] * len(possible_attributes) for _ in range(len(possible_labels))]
			att_values = np.array(att_values)
		
			cond = [att_values == a for a in possible_attributes]
			labels = np.array(self.labels)

			filtered_labels = [labels[c] for c in cond]

			for i, l in enumerate(possible_labels):
				for j, a in enumerate(possible_attributes):
					for lbl in filtered_labels[j]:
						if lbl == l:
							branches[i][j] += 1

			entropies.append(conditional_entropy(branches))	

		best_split = np.argmin(entropies)
		self.dim_split = best_split

		############################################################
		# TODO: split the node, add child nodes
		############################################################
		feature = np.unique(np.array(self.features)[:, best_split])
		self.feature_uniq_split = list(feature).copy()
		for f in feature:
			child_features = []
			child_labels = []
			for i, sample in enumerate(self.features):
				if sample[best_split] == f:
					copied = sample.copy()
					child_features.append(copied[:best_split]+copied[best_split + 1:])
					child_labels.append(self.labels[i])
			# import ipdb
			# ipdb.set_trace()
			self.children.append(TreeNode(child_features, child_labels, int(np.unique(child_labels).shape[0])))

		# split the child nodes
		for child in self.children:
			if child.splittable:
				child.split()

		return

	def predict(self, feature: List[int]) -> int:
		if self.splittable:
			# print(feature)
			idx_child = self.feature_uniq_split.index(feature[self.dim_split])
			feature = feature[:self.dim_split] + feature[self.dim_split+1:]
			return self.children[idx_child].predict(feature)
		else:
			return self.cls_max



