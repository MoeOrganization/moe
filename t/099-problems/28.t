# P28 (**) Sorting a list of lists according to length of sublists.

#     a) We suppose that a list contains elements that are lists
#     themselves. The objective is to sort the elements of the list
#     according to their length. E.g. short lists first, longer lists
#     later, or vice versa.

#     Example:

#     moe> lsort([['a', 'b', 'c'], ['d', 'e'], ['f', 'g', 'h'], ['d', 'e'], ['i', 'j', 'k', 'l'], ['m', 'n'], ['o']])
#     [['o'], ['d', 'e'], ['d', 'e'], ['m', 'n'], ['a', 'b', 'c'], ['f', 'g', 'h'], ['i', 'j', 'k', 'l']]

#     b) Again, we suppose that a list contains elements that are
#     lists themselves. But this time the objective is to sort the
#     elements according to their length frequency; i.e. in the
#     default, sorting is done ascendingly, lists with rare lengths
#     are placed, others with a more frequent length come later.

#     Example:

#     moe> lsort_freq([List('a', 'b', 'c'], ['d', 'e'], ['f', 'g', 'h'], ['d', 'e'], ['i', 'j', 'k', 'l'], ['m', 'n'], ['o']))
#     [['a', 'b', 'c'], ['f', 'g', 'h'], ['i', 'j', 'k', 'l'], ['m', 'n'], ['o'], ['d', 'e'], ['d', 'e']]

#     Note that in the above example, the first two lists in the
#     result have length 4 and 1 and both lengths appear just
#     once. The third and fourth lists have length 3 and there are two
#     list of this length. Finally, the last three lists have length
#     2. This is the most frequent length.

use Test::More;

sub lsort(@list) {
    @list.sort(($a, $b) => { $a.length <=> $b.length })
}

sub lsort_freq(@list) {
    my %freq = {};
    @list.each(($l) => { %freq{~$l} = +%freq{~$l} + 1; });

    # second criterion added to fix the ordering of same-frequency lists
    @list.sort(($a, $b) => { %freq{~$a} <=> %freq{~$b} || ~$a cmp ~$b });
}

my @list = [['a', 'b', 'c'], ['d', 'e'], ['f', 'g', 'h'], ['d', 'e'], ['i', 'j', 'k', 'l'], ['m', 'n'], ['o']];
my @sorted_1 = [['o'], ['d', 'e'], ['d', 'e'], ['m', 'n'], ['a', 'b', 'c'], ['f', 'g', 'h'], ['i', 'j', 'k', 'l']];
my @sorted_2 = [['a', 'b', 'c'], ['f', 'g', 'h'], ['i', 'j', 'k', 'l'], ['m', 'n'], ['o'], ['d', 'e'], ['d', 'e']];

ok(lsort(@list).eqv(@sorted_1),      "... P28-1");
ok(lsort_freq(@list).eqv(@sorted_2), "... P28-2");

done_testing();
