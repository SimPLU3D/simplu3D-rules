Simplu3D-rules
============

A library to automatically check respect of local urban  regulations concerning constructability.


Introduction
---------------------

This research library is developed as part of [COGIT laboratory](http://recherche.ign.fr/labos/cogit/accueilCOGIT.php) researches concerning processing of urban regulation. 

It provides 3D geographic models in order to automatically check morphological rules from local urban regulation (i.e. maximal height, distances between objects) expressed in proposed formalizations. These functionalities allow many possible applications such as generation of building layout to test several regulation scenarios, assisting architect in building design or simplify construction permit inspection.

The project is developed over 3D GIS Open-Source library [GeOxygene](http://oxygene-project.sourceforge.net) concerning geometric operators and 3D visualization. The project contains two independent modules for rules management in two different packages:
* package gru3D : a relatively simple model based an XML description of the rules with a GUI to handle rules ([Brasebin, 2011](http://recherche.ign.fr/labos/cogit/publiCOGITDetail.php?idpubli=4120&portee=chercheur&id=59&duree=100&nomcomplet=Brasebin;Mickael));
* package simplu : a more advanced model with rules described in [OCL language](http://www.omg.org/spec/OCL/) that allows a great expressivity ([Brasebin, 2014](http://recherche.ign.fr/labos/cogit/publiCOGITDetail.php?idpubli=5016&portee=chercheur&id=59&duree=100&nomcomplet=Brasebin;Mickael))  .

Conditions for use
---------------------
This software is free to use under CeCILL license. However, if you use this library in a research paper, you are kindly requested to acknowledge the use of this software.

Furthermore, we are interested in every feedbacks about this library if you find it useful, if you want to contribute or if you have some suggestions to improve it.

Library installation
---------------------
The project is build with Maven and is coded in Java (JDK 1.7 is required), it has been tested in most common OS. If you are not familiar with Maven, we suggest installing developer tools and versions as described in [GeOxygene install guide](http://oxygene-project.sourceforge.net/documentation/developer/install.html).

Packages execution
---------------------

Two demonstration classes are present :
* for gtru3D package : Demo class at the root of gtru3D package; 
* for simplu package : TestOCL class in package test.checker allows the checking of OCL file  (test/resources/ocl/ocl/simple_allConstraintsThese.ocl).

Contact for feedbacks
---------------------
[Mickaël Brasebin](http://recherche.ign.fr/labos/cogit/cv.php?nom=Brasebin) & [Julien Perret](http://recherche.ign.fr/labos/cogit/cv.php?prenom=Julien&nom=Perret)
[Cogit Laboratory](http://recherche.ign.fr/labos/cogit/accueilCOGIT.php)


Acknowledgments
---------------------

+ This research is supported by the French National Mapping Agency ([IGN](http://www.ign.fr))
+ It is partially funded by the FUI TerraMagna project and by Île-de-France
Région in the context of [e-PLU projet](www.e-PLU.fr)

