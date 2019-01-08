import numpy as np
from typing import List, Set

from classifier import Classifier
from decision_stump import DecisionStump
from abc import abstractmethod

class Boosting(Classifier):
  # Boosting from pre-defined classifiers
	def __init__(self, clfs: Set[Classifier], T=0):
		self.clfs = clfs      # set of weak classifiers to be considered
		self.num_clf = len(clfs)
		if T < 1:
			self.T = self.num_clf
		else:
			self.T = T
	
		self.clfs_picked = [] # list of classifiers h_t for t=0,...,T-1
		self.betas = []       # list of weights beta_t for t=0,...,T-1
		return

	@abstractmethod
	def train(self, features: List[List[float]], labels: List[int]):
		return

	def predict(self, features: List[List[float]]) -> List[int]:
		'''
		Inputs:
		- features: the features of all test examples
   
		Returns:
		- the prediction (-1 or +1) for each example (in a list)
		'''
		########################################################
		# TODO: implement "predict"
		########################################################
		N = len(features)
		predicts = [0 for i in range(N)]
		for i in range(self.T):
			pred_i = self.betas[i] * np.array(self.clfs_picked[i].predict(features))
			predicts = [predicts[j] + pred_i[j] for j in range(N)]
		return list(np.sign(predicts))

class AdaBoost(Boosting):
	def __init__(self, clfs: Set[Classifier], T=0):
		Boosting.__init__(self, clfs, T)
		self.clf_name = "AdaBoost"
		return
		
	def train(self, features: List[List[float]], labels: List[int]):
		'''
		Inputs:
		- features: the features of all examples
		- labels: the label of all examples
   
		Require:
		- store what you learn in self.clfs_picked and self.betas
		'''
		############################################################
		# TODO: implement "train"
		############################################################
		
		N = len(labels)

		w = np.full(shape=N, fill_value=1. / N)

		for t in range(self.T):

			# Find h_t
			H = list()
			clfs = list(self.clfs)
			for clf in clfs:
				H.append(clf.predict(features))

			errors = list()
			raw_errors = list()
			for h in H:
				err = (np.array(labels) != np.array(h)).astype(int)
				raw_errors.append(err)
				errors.append(np.sum(w * err))

			min_idx = np.argmin(errors)
			h_t = clfs[min_idx]
			err_t = errors[min_idx]
			errors = raw_errors[min_idx]
			self.clfs_picked.append(h_t)

			beta_t = 1 / 2 * np.log((1 - err_t) / err_t)
			self.betas.append(beta_t)
			for i in range(N):
				if errors[i]:
					w[i] = w[i] * np.exp(beta_t)
				else:
					w[i] = w[i] * np.exp(-beta_t)
			w = w / np.sum(w)

	def predict(self, features: List[List[float]]) -> List[int]:
		return Boosting.predict(self, features)



	