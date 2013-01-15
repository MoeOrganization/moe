# Moe

An -OFun prototype of an Ultra Modern Perl 5 written in Scala.

## What is this??

It is simplest perhaps to start by saying what this project is not,
it is *not* an attempt to create a production version of Perl 5 on the
JVM.

Instead, this project is an /experiment/ to see what a cleaned up and
slimmed down Perl 5 might look like. The key word here is "experiment",
because no one knows exactly what that will look like, and I want to
try and find out.

I will be running this project in the spirit of the Pugs project
(Perl 6 on Haskell), which means it is -OFun (optimized for fun)
and I welcome any and all contributors. This *must* be a community
effort for it to have any chance of success.

## Inspiration

A sizable portion of the inspiration for this project came from a talk
given by Jesse Vincent called "Perl 5.16 and Beyond" in which he spoke
about a slimmer Perl 5 core which no longer included things like
formats, Unix group and user functions, sockets, etc. He also talked
about the idea of breaking some backwards compatibility (in a very
controlled and clean way) and allowing for new features to be added
to the core language. This talk really struck home because I had
discussed exactly this same thing with many people over the years.

This project is an attempt at doing a lot of what Jesse was talking
about, but instead of trying to clean up the current Perl 5 core,
I wanted to see what would happen if we started from scratch.

## What about Perl 6

I believe that Perl 6 is ultimately the true future of Perl, and that
the language that Larry is so carefully designing will be a really
great and fun language to hack in. I got a taste for it when I worked
on the Pugs project (Perl 6 in Haskell) and I have spent a lot of my
time since trying to bring Perl 6 inspired ideas back to Perl 5 (Moose
being the perfect example of this).

So why am I not directing my efforts towards Perl 6?

I believe that it will ultimately benefit Perl 6 to have a more modern
Perl 5 that it can interface with. This was something that the Perl 6
folks understood as well, and was manifested in the Ponie project
(Perl 5 on Parrot). Unfortunately the Ponie project did not succeed,
however the crux of the idea I think is still a good one.

Additionally, I plan to work closely with the Perl 6 team to make
sure anything we do make here is poised to mesh well with what the
Perl 6 team is doing.

## Wait, I have more questions?!?!

I have started an FAQ document to help answer questions about this
project, I suggest you refer there. If your questions are not
answered in that document, please either submit an issue or better
yet fork the project, add the question, seek out an answer and
submit a pull request.

## Project Status

This is a very, very, very early stage of this project, so please do
not judge too harshly. I have created a STATUS document that will
describe in more detail where the project is and also start to lay
out things that need to be done.

## How you can help

As I said above, this project will be run "Pugs style", so I welcome
any and all contributions. I should warn you though, there will likely
be a lot of churn and duplicated efforts in the early days as this
project gets its footing.

### Communication

Currently the best place to discuss this is in the #moe channel on
irc.perl.org. Github issues are also a perfectly valid means of
communication. As the project matures this all will evolve more,
but this should be sufficient for now.

### Hacking on this

Here is what you need installed to get started.

* Java (preferably 7)
* SBT (Simple Build Tool, get it from
  http://www.scala-sbt.org/release/docs/Getting-Started/Setup.html)

To compile the code and run the test suite, you need only type the
following at the root of the source tree.

```
sbt test
```

As is the convention with git based projects, please fork and send
pull requests.

### What can I do?

Refer to the STATUS document for information regarding this.

