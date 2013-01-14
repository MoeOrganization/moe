# Moe Frequently Asked Questions

This document will contain answers to questions that either have
already come up, or which I anticipate will come up. If your
questions are not answered in that document, please do one of
the following:

* submit an issue on Github asking the question
* fork the project, add the question to this file, seek out an
answer, add it in and submit a pull request

The second option is the more preferred approach of course.

## How much of Perl are you planning to stay compatible with?

A subset.

Exactly what subset, at this point I can not say. This should
shake out in the next week or so as I try and spec out the
language subset.

## Are you going to support XS

Don't you think it is a little early to start asking that?

Actually, my (very, very, very) rough plans for backcompat is to
lean on an embeddable Perl interpreter somehow (libperl.so). It
does mean there is some kind of walled garden in between (old)
Perl 5 and (new) Perl 5, but I don't believe that we could do it
any other way. Over time I believe it will be possible to evolve
something closer to what Jesse Vincent proposed in this "5.16 and
Beyond" talk, but having that as a starting goal I think is
unreasonable and unrealistic.

## What makes you think you can do this?

Honestly, I don't know if I can, but you never know unless you try.

## Why not C?

To start with, I don't know C, and to be honest I am not all that
interested in learning it.

Additionally I think that starting it in C would be premature
optimization. It also defeats the whole "would it be nice to run
on multiple VMs" idea.

Does that mean I am against using C? No, perhaps if this all
works out, the final version will be in C, which I am fine with
as long as it is a compiler and can target multiple runtimes.

## Why not Perl 5?

I don't think Perl 5 is actually a great language to write a
language in. I really wanted something with a solid type system
that is statically checked. That said, I am not against eventually
being self-hosting or something.

## Why Scala?

So, see my above comment about a nice static type system, which
Scala has and which I find much nicer then other languages like
Java.

In fact, I originally started to write this in Java, after looking
over a few different languages, but after two rough implementations
I kept finding myself running into Java's annoying type system.

I am becoming quite fond of Scala for the following reasons:

* It can interoperate with Java and that entire ecosystem
* It is enough of a functional programming language to be really
  nice for writing compilers in, and enough of an OOP language to
  be easy for those not as familiar with FP
* The language actually feels Perl-ish
* It is a useful skill, so if Perl is really dead, then we can all
  learn a new skill and move on ;)

## Why not $my_favorite_language?

See above.

## Are you planning on sticking with the JVM?

No, not necessarily, the only reason the JVM is involved right
now is that I am writing this in Scala. I don't plan on only
having an interpreter, eventually I would love have a compiler
and then we can target multiple VMs.

## Why do you have a simplistic tree-walking interpreter?

This is the shortest path to a working language that people can
play with, anything else would be premature optimization.

I do know that an AST interpreter is slow as dirt, but I also
know that having an AST makes it easier to write a true compiler
as well. I also know that a stable AST is very important for good
tooling support (IDE autocompletes, etc).

## Moe?

Modern Perl (Moose) - OS dependency (the JVM) = Moose - OS = Moe

## Perl is Dead

Yes, I know, I wrote a talk about it.

But if you are just looking to troll, you can go to reddit,
hackernews or whatever, that is what they are there for.

