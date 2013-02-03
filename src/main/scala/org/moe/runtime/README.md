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

This is the main runtime object, it 