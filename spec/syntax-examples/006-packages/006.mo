package Bar {

    my $foo = 10;
    my @bar = [];
    my %baz = {};

    sub foo () { $foo }

    sub bar {
        return @bar;
    }

    sub baz {
        if ( $foo ) {
            return %baz;
        }
    }
}