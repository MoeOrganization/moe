# Moe

An -OFun prototype of an Ultra Modern Perl 5 written in Scala.

## Wait, what are you talking about?

Over time there will be more and more information in this README, but 
for the time being I will start by sharing an email discussion I had 
with Nicolas Clark about what it is that I am trying to do here.

```
>> On Jan 12, 2013, at 1:10 PM, Nicholas Clark <nick@ccl4.org> wrote:
> Quite possibly I'm telling you things you already know,

Honestly, anything you tell me will almost certainly contain a lot of 
things I don't know. I have never attempted to write a language outside of
a few simple lambda calculus interpreters I wrote a few years back. With 
my project, I hope to attract others (both inside and outside the perl 
community) to hack on this.

I do not in any way believe that what I release this afternoon will be the 
final product, my primary goal right now is to spur innovation and bring 
the discussion of a "new core" out from the dark corners and into the full 
light of day. 

> 
> but strikes me that there is at least some level of distinction between 
> parser/codegen, VM, and runtime libraries.

Right now I have the following:

- a runtime, which I am fairly happy with (still needs some work, but I 
  think the core is where I would like it to be)
- an AST, which I know is still missing a lot of stuff, but again I have 
  a foundation
- an Interpreter, this just evaluates the AST and is only like 20% of the 
  AST at the moment, but is a start (and an easy place for people to 
  start hacking)
- no parser at all to speak of

I know that an AST interpreter is slow as dirt, but I also know that having 
an AST makes it easier to write a true compiler as well (possibly to target 
multiple VMs etc). I also know that a stable AST is very important for good 
tooling support (IDE autocompletes, etc). 

I originally started to write this in Java, after looking over a few different 
languages, but after two rough implementations I kept finding myself running 
into Java's annoying type system. So I have settled now on Scala, for a few 
of reasons:

1) It can interoperate with Java and that entire ecosystem
2) It is enough of a functional programming language to be really nice for 
   writing compilers in, and enough of an OOP language to be easy for those 
   not as familiar with FP
3) The language actually feels Perl-ish 
4) It is a useful skill, so if Perl is really dead, then we can all learn 
   a new skill and move on ;)

> In that it might be that one approach is to try to get something that parses
> Perl 5 (or a subset), but generates optrees to use on the current VM.
> Which keeps you in with all the XS code on CPAN. And all the quirks of all
> the PP* functions.

Actually, my (very, very, very) rough plans for backcompat is to lean on an 
embeddable Perl interpreter somehow. It does mean there is some kind of walled 
garden in between (old) Perl 5 and (new) Perl 5, but I don't believe that we 
could do it any other way. Over time I believe it will be possible to evolve 
something closer to what Jesse Vincent proposed in this "5.16 and Beyond" talk, 
but having that as a starting goal I think is unreasonable and unrealistic. 

> Also, approaches I'd consider. Which you may have already thought about
> 
> 1) I'm curious how well Marpa could parse Perl

Yes, I am too. But my parser experience is limited so I am hoping to get 
others to help out there.

> 2) I'm curious whether Rakudo's grammar for Perl 6 can be derived from, and
>   mutated to be more Perl 5-like
>   I suspect that this would be the fastest way onto the JVM.
>   (and anything else that Rakudo ends up targeting. There's a JS port of
>   NQP active, and .NET isn't going to be hard once the JVM is conquered.)
>   And, obviously, this gets you onto Parrot right away.

Yes, so I had a number of conversations with Patrick and Jonathan about 
Raduko/NPQ/etc. regarding this. Right now there is not a sufficient level 
of maturity in these tools that I was willing to use it as a foundation.

> 3) No idea if it's viable to take the output of PPI and build a parse tree
>  from its output

Actually that is a decent idea, I had not thought of that.

> 4) https://github.com/fglock/Perlito
>   It's not like there isn't a working prototype for this already. :-)

Oh, yes, Flavio was a major inspiration to this and I plan on trying 
to cajole him into contributing ASAP.

> Feel free to share, if you think that it's helpful.

I might, I need to actually write up a README file for the repo so I 
might use this email for that.

> Nick
> 
> PS You're on at 5pm. That's EST? If so, that's 11pm here. I'll be asleep.
>   And very asleep during any subsequent discussions.

Well, I will be out doing Lazer-Tag and Whirlyball in the OPW after 
party, so I will just let the discussions just happen and check things 
in the morning. :)

Thanks again for your advice and ideas, 

- Stevan
```

## Status of this

This is at a very, very, very early stage, please don't judge it too 
harshly. Nuff said.

## Hacking on this

So if anyone is interested in hacking on this, here is what you need
installed to get started.

* Scala (latest stable is fine)
* SBT (Scala Build Tool)

To compile the code and run the test suite, you need only type the 
following at the root of the source tree.

```
sbt test
```

As is the convention with git based projects, please fork and send
pull requests.

I don't have a formal plan yet, so I don't have a formal list of 
TODO items either. I have tried to make sure there is a test file 
for most every component in the system and there is most certainly 
more tests that can be written. 

## Discussion of this 

I have created no formal communications channel for discussing this 
project, that will come over the next few days/weeks as needs dictate. 



