
use Tree;

my $t = ^Tree.new("0.0", [
    ^Tree.new("1.0", [
        ^Tree.new("1.1"),
        ^Tree.new("1.2", [
            ^Tree.new("1.2.1"),
            ^Tree.new("1.2.2"),
        ])
    ]),
    ^Tree.new("2.0", [
        ^Tree.new("2.1", [
            ^Tree.new("2.1.1", [
                ^Tree.new("2.1.1.1"),
            ]),
            ^Tree.new("2.1.2", [
                ^Tree.new("2.1.2.1"),
                ^Tree.new("2.1.2.2"),
            ])
        ])
    ]),
    ^Tree.new("3.0"),
]);

$t.traverse(-> ($c) {
   say( (".." x $c.depth), $c.node )
});

