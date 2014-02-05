use Test::More;

package Bar {
    sub quux { __PACKAGE__() }
}

is(Bar::quux(), "Bar", '__PACKAGE__ returns what we expect');

done_testing();
