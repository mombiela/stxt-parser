# Description

This is a parser for the STxT language: http://www.semantictext.info

It's implemented in Java. Only jre8 required, not other dependencies.

# Manual

Import jar: [stxt-1.0.0.jar](https://github.com/mombiela/stxt-parser/raw/master/dist/stxt-1.0.0.jar)

To parse a STxT document:

info.semantictext.Node stxtNode = info.semantictext.parser.FileParser.parse(file);

## Java Classes Overview (IA Generated, be careful!)

Below is a brief overview of the Java classes included in this repository:

### `NodeToGrammar.java`

This class translates a custom-defined syntax tree structure into a grammar representation. It processes nodes, their attributes, and child nodes to generate a list of `NamespaceNode` objects that define the grammar rules. The class provides utility methods for creating grammar-related objects.

### `RootGrammar.java`

This class generates a predefined root grammar for a specific namespace. It defines grammar rules for various types of nodes, such as type definitions, children, and namespace definitions. The grammar rules are organized into `NamespaceNode` objects.

### `NodeValidator.java`

This class validates nodes parsed based on predefined grammar rules. It performs validation for different data types like binary, boolean, integer, hexadecimal, etc. The class ensures that the parsed nodes conform to the specified grammar rules and data type patterns.

### `Parser.java`

This class is responsible for parsing input streams containing text-based syntax tree definitions. It reads lines from the input, processes them, and generates a structured representation of the syntax tree using `Node` objects. The class maintains a stack of nodes to handle nesting and hierarchy.
