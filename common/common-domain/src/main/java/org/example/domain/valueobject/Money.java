package org.example.domain.valueobject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Money {
    private final BigDecimal amount;

    public static final Money ZERO = new Money(BigDecimal.ZERO);

    public Money(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * amountが0より大きいかの比較.
     *
     * @return true/false
     */
    public boolean isGreaterThanZero() {
        return this.amount != null && this.amount.compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * amountと引数のMoneyの比較.
     *
     * @param money 値段
     * @return true/false
     */
    public boolean isGreaterThan(Money money) {
        return this.amount != null && this.amount.compareTo(money.getAmount()) > 0;
    }

    /**
     * amountと引数のMoneyの加算.
     *
     * @param money 値段
     * @return 加算後の値段
     */
    public Money add(Money money) {
        return new Money(setScale(this.amount.add(money.getAmount())));
    }

    /**
     * amountと引数のMoneyの減算.
     *
     * @param money 値段
     * @return 減算後の値段
     */
    public Money subtract(Money money) {
        return new Money(setScale(this.amount.subtract(money.getAmount())));
    }

    /**
     * amountと引数のMoneyの乗算.
     *
     * @param multiplier 乗数
     * @return 乗算後の値段
     */
    public Money multiply(int multiplier) {
        return new Money(setScale(this.amount.multiply(new BigDecimal(multiplier))));
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return Objects.equals(amount, money.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }

    /**
     * 少数点第2位で少数切り捨て.
     *
     * @param input 入力値
     * @return 変換後の値
     */
    private BigDecimal setScale(BigDecimal input) {
        return input.setScale(2, RoundingMode.HALF_EVEN);
    }
}
