# Description

This is a parser for the STxT language: http://www.semantictext.info

It's implemented in Java. Only jre5 required, not other dependencies.

# Manual

Import jar: tree/master/dist/stxt-1.0.0.jar

To parse a STxT document:

info.semantictext.Node stxtNode = info.semantictext.parser.FileParser.parse(file);