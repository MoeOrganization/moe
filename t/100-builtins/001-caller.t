use Test::More;

sub foo { [ caller() ] } 
sub bar { [ caller(), foo(), caller() ] } 
sub baz { [ caller(), bar(), caller() ] }
    
{
    is_deeply(
        baz(), 
        [
            [ ["main", "baz"] ],
            [
                [ ["main", "bar"], ["main", "baz"] ],
                    [
                        [ ["main", "foo"], ["main", "bar"], ["main", "baz"] ]
                    ],
                [ ["main", "bar"], ["main", "baz"] ]
            ],
            [ ["main", "baz"] ]
        ],
        '... got the results of deep calls correctly'
    );
}

sub one   ($x) { caller($x) }
sub two   ($x) { one($x)    }
sub three ($x) { two($x)    }
sub four  ($x) { three($x)  }

{
    is_deeply(four(0), ["main", "one",    ], '... got the right stack trace info for level 0');
    is_deeply(four(1), ["main", "two",    ], '... got the right stack trace info for level 1');
    is_deeply(four(2), ["main", "three",  ], '... got the right stack trace info for level 2');
    is_deeply(four(3), ["main", "four",   ], '... got the right stack trace info for level 3');
    is(four(4), undef,    '... got the right stack trace info for level 4');
}

package Foo { 
    sub bar { caller() } 
    sub baz { bar() } 
    package Bar {
        sub gorch { Foo::baz() } 
    }
}

{
    is_deeply(
        Foo::baz(),
        [["Foo", "bar"], ["Foo", "baz"]],
        '... tracking packages properly'
    );

    is_deeply(
        Foo::Bar::gorch(),
        [["Foo", "bar"], ["Foo", "baz"], ["Foo::Bar", "gorch"]],
        '... tracking packages properly'
    );
}

class CallerTest {
    method one   { caller() }
    method two   { self.one }
    method three { self.two }
}

{
    my $t = CallerTest.new;
    is_deeply(
        $t.three,
        [["CallerTest", "one"], ["CallerTest", "two"], ["CallerTest", "three"]],
        '... tracking classes and methods properly'
    );
}


done_testing();

