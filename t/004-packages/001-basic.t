use Test::More;

package Foo {
    sub bar { "Foo::bar" }
    sub baz { bar() ~ "::baz" }
    sub gorch { Foo::baz() ~ "::gorch" }
}

eval_lives_ok("Foo::bar()", '... calling a sub in a package works');

is(Foo::bar(),   "Foo::bar", '... and that sub does what we expect');
is(Foo::baz(),   "Foo::bar::baz", '... subs can call other subs locally inside the package');
is(Foo::gorch(), "Foo::bar::baz::gorch", '... subs can call other subs fully qualified inside the package');

done_testing();