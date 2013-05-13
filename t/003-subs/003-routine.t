use Test::More;

{
    my &fib = -> ($n) { 
        if ($n < 2) { 
            $n 
        } else { 
            (&?ROUTINE.call($n - 1)) + (&?ROUTINE.call($n - 2))
        } 
    };

    is(&fib.(10), 55, '... fixed point fibonacci');
}

{ 
    sub foo { &?ROUTINE.name }
    is(foo(), "foo", '... calling meta methods on the &?ROUTINE');
}

done_testing();