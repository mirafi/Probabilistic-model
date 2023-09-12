# Entropy
        int setOfSample = 10; 
        DataTable dt = SampleData.dataSetPayTennis(setOfSample); 
        //  dt.print();
        
        // Create object
        Entropy entropy = new Entropy(_ROOT_, dt);
        
        // Calculate information gain
        entropy.init();
        
        // Find the root node for decision tree information gain
        Node root = entropy.buildTree();
        
        // Create DecisionsTree to find result
        DecisionsTree decisionsTree = new DecisionsTree(root);
        
        // Print tree 
        decisionsTree.print();
        
        // Test data
        List<Entity> entities = new ArrayList<>();

        entities.add(new Entity("humid","normal"));
        entities.add(new Entity("outlook","sunny"));

        // Traverse the decision tree
        Node d = decisionsTree.traverse(decisionsTree.getRoot(),entities);
        
        System.out.println(d.getGoalValue());

        


# Markov chain

# Probabilistic-model

