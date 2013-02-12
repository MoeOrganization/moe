
- create a MoeExceptionObject class to wrap native exceptions

- think about how to implement operators so they are polymorphic
  - SEE ALSO: https://github.com/rakudo/rakudo/tree/nom/src/core

- make all the tests to use the ShouldMatchers

[DONE]
- we should remove all exceptions in the org.moe.runtime._ classes
    - turn then into Option[T] instead
    - let all exceptions all be handled in the Interpreter
        [TODO]
        - this will add more work for the Interpreter, but 
          it will centralize all error handleing which will
          be better in the long run
        [TODO]
        - it should also be possible for there to be a post-parse
          but pre-execution phase which could actually perform some
          static checks on things like method lookup and variable
          lookup, etc. 
            - this could catch errors earlier then runtime which
              would have previously been caught at runtime

[DONE]
- MoeRuntime should not be a singleton object
    - Interpreter should then use an instance of MoeRuntime

- Interpreter should not be a singleton object
    - the Moe singleton object should hold an instance to it
    - this might be useful for threading and forking and such

[DONE]
- MoeObject should not accept a string for method calls
    - it should accept a MoeMethod
        [TODO]
        - which can be looked up by the Interpreter and pulled
          from the assocaitedClass at that point
