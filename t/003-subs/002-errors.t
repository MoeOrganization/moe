use Test::More;

sub id ($x) { $x }

eval_dies_ok("id()", '... not enough arguments provided fails correctly');
eval_dies_ok("id(1, 2)", '... too many arguments provided fails correctly');

eval_lives_ok("id(1)", '... right number of arguments succeeds correctly');

done_testing();