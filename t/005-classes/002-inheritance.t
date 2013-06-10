use Test::More;

class BankAccount {
    has $!balance = 0;

    method balance { $!balance }

    method deposit ($amount) {
        $!balance = $!balance + $amount;
    }

    method withdraw ($amount) {
        if ($!balance < $amount) {
            die "Account overdrawn";
        }
        $!balance = $!balance - $amount;
    }
}

class CheckingAccount extends BankAccount {
    has $!overdraft_account;

    method withdraw ($amount) {
        my $overdraft_amount = $amount - $!balance;

        if ($!overdraft_account.defined && $overdraft_amount > 0) {
            $!overdraft_account.withdraw($overdraft_amount);
            self.deposit($overdraft_amount);
        }

        super;
    }
}

{
    my $savings = BankAccount.new( balance => 100 );
    ok($savings.isa("BankAccount"), '... got an instance of BankAccount');

    is($savings.balance, 100, '... got the expected balance');
    
    eval('$savings.balance(100)');
    ok($!.defined, '... accessor is read only');

    eval('$savings.deposit(200)');
    ok(not($!.defined), '... deposit worked');

    is($savings.balance, 300, '... got the expected (altered) balance');

    eval('$savings.withdraw(50)');
    ok(not($!.defined), '... withdraw worked');

    is($savings.balance, 250, '... got the expected (altered) balance');

    eval('$savings.withdraw(350)');
    ok($!.defined, '... withdraw worked');    

    is($savings.balance, 250, '... got the expected (unaltered) balance');
}

{
    my $savings = BankAccount.new( balance => 100 );
    ok($savings.isa("BankAccount"), '... got an instance of BankAccount');    

    my $checking = CheckingAccount.new( 
        balance           => 250,
        overdraft_account => $savings 
    );
    ok($checking.isa("CheckingAccount"), '... got an instance of CheckingAccount');        
    ok($checking.isa("BankAccount"), '... it is also derived from BankAccount');        

    is($checking.balance, 250, '... got the expected (checking) balance');
    is($savings.balance, 100, '... got the expected (savings) balance');

    eval('$checking.withdraw(200)');
    ok(not($!.defined), '... withdraw did not die');

    is($checking.balance, 50, '... got the expected (checking) balance');
    is($savings.balance, 100, '... got the expected (savings) balance');

    eval('$checking.withdraw(100)');
    ok(not($!.defined), '... withdraw did not die');

    is($checking.balance, 0, '... got the expected (checking) balance');
    is($savings.balance, 50, '... got the expected (savings) balance');

    eval('$checking.withdraw(100)');
    ok($!.defined, '... withdraw did die this time');

    is($checking.balance, 0, '... got the expected unaltered (checking) balance');
    is($savings.balance, 50, '... got the expected unaltered (savings) balance');    
}

done_testing();
