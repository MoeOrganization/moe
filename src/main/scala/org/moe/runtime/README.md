# Moe Runtime

This document attempts to describe the structure and philosophy 
of the Moe Runtime. 

## MoeSystem

This class is meant to wrap some basic runtime features so that 
we can control/configure it within our runtime. 

It only contains just the basics right now, but eventually this 
should be the primary way in which our runtime interfaces with it's
host runtime.

## MoeRuntime

This is the main runtime class, it takes an instance of MoeSystem 
as a parameter. The `bootstrap` method must be called before anything
useful can be done with this.

## MoeEnvironment

This represents a lexical environment.

## MoeErrors

This is a set of exceptions that can be used within Moe

## MoeObject

This represents an object which may or may not have an associated
class, and has a numeric ID associated with it.

## MoeOpaque

This is a subclass of MoeObject and adds the ability to store 
instance values.

## MoeNativeObject

This is a base class and several implementations for the native
objects that Moe supports.

## MoePackage

This represents a package in Moe. Packages can contain subroutines,
classes and sub-packages (all in seperate namespaces (at least for 
now they are, this might change)). Packages are also aware of thier
parent packages. Packages also have an associated MoeEnvironment 
instance which is the environment the package was compiled with (
this is useful for closures, etc.)

### MoeSubroutine

This represents a subroutine in Moe.

## MoeClass

This represents a class in Moe. Classes have associated attributes 
and methods, and may or may not have an associated superclass.

### MoeAttribute

This represents a class attribute in Moe.

### MoeMethod

This represents a class method in Moe.


