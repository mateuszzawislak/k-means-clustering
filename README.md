Constrained K-means Clustering with Background Knowledge
============================================

Java implementation of data mining algorithm "Constrained K-means Clustering with Background Knowledge".

Aim of the project
------------------

The aim of the project was to create a program that implements the Constrained K-means Clustering with Background Knowledge algorithm and examine its performance.

[Algorithm article (Constrained K-means Clustering with Background Knowledge)](https://web.cse.msu.edu/~cse802/notes/ConstrainedKmeans.pdf)

Program description
------------------

CSV files - transactional data source
------------------

A set of transactions undergoing a process of clustering is taken by the program from .csv file. The program assumes that:
* data columns in .csv file are separated by the sign indicated in configuration file,
* data are complete (i.e. does not provide value unknown),
* all rows in .csv file have the same number of columns.

Sets of constraints (mustlink and cannotlink) are also loaded from .csv files. All rows of those files must have two columns. For mustlink constraints they mean transactions identifiers (i.e. rows numbers of .csv file with transactional data. Rows are counted from 0.), which must be in the same cluster. For constraints cannotlink it is analogous.

Configuration file
------------------

The configuration file allows you to specify data sources and a lot of clustering algorithm’s parameters. While executing the program the path to the configuration file must be placed in the environment variable pl.edu.pw.elka.mzawisl2.ckmc.config.

The following table describes all the parameters of the configuration file:

|Parameter|Type|Example value|Description
|------|------|------|------
|*cluster.k.number*|Integer|`2`|number of clusters
|*cluster.max.reclustering.number*|Integer|`200`|maximal number of reclustering (the stop condition of the algorithm)
|*cluster.drift.tolerance*|Float|`10.0`|acceptable change in the position of centroid of each cluster (the stop condition of the algorithm)
|*dictionary.distance.wage*|Float|`20.0`|distance value of different values for the same dictionary
|*csv.file.name*|String|`D:\\data.csv`|source file with set of transactions
|*csv.split.by*|String|`,`|separator sign in .csv file
|*csv.clusterable.columns*|String|`0,1,2,3,5`|identifies of columns, which should be included in clustering algorithm - `default value = ALL columns identifiers`
|*csv.dictionary.columns*|String|`0,2`|identifies of columns, which should be treated as dictionaries
|*csv.constraints.must.link.file.name*|String|`D:\\must.csv`|path to file containing mustlink constraints
|*csv.constraints.can.not.link.file.name*|String|`D:\\cannot.csv`|path to file containing cannotlink constraints
