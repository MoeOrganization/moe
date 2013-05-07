use Test::More;

sub id ($x) { $x }

{
    eval_dies_ok("id()", '... not enough arguments provided fails correctly');
    eval_dies_ok("id(1, 2)", '... too many arguments provided fails correctly');

    eval_lives_ok("id(1)", '... right number of arguments succeeds correctly');
}

sub want_array (@a) { @a.length }

{
    eval_dies_ok("want_array(1)", '... incorrect argument types');
    eval_dies_ok("want_array('foo', 'bar')", '... incorrect argument types (and too many)');
    eval_dies_ok("want_array({})", '... incorrect argument types');

    eval_lives_ok("want_array([ 1, 2, 3 ])", '... correct argument types');
}

done_testing();