# PPL A8

This assignment creates an abstract syntax tree of a subset of Scheme called ``` Scheme-- ``` .


## Compiling and Running

To build, type
```
make build
```
To build and run, type 
```
make run
```

The program can also be run directly with
```
javac -d bin -classpath bin src/*/*.java
java -classpath bin ast.ASTBuilder input.txt
```

## Description

The program scans and tokenizes the input using regular expressions.
Then the tokenized output is piped to the Parser,
which implements a recursive descent parser using a transformed grammar.
The grammar is hard coded in the program, 
in the NonTerminal class.
Then an ASTBuilder takes the ParseTree and creates an AbstractSyntaxTree.
Each Node in the AST is an ```Expression```.
There is an ```eval()``` function which should do most of the 
translation from ParseTree to AbstractSyntaxTree.


