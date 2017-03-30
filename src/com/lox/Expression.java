package com.lox;

import java.util.List;

abstract class Expression {

    static class Binary extends Expression{
        Binary(Expression left, Token operator, Expression right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }

        final Expression left;
        final Token operator;
        final Expression right;
    }

    static class Grouping extends Expression{
        Grouping(Expression expression) {
            this.expression = expression;
        }

        final Expression expression;
    }

    static class Literal extends Expression{
        Literal(Object value) {
            this.value = value;
        }

        final Object value;
    }

    static class Unary extends Expression{
        Unary(Token Operator, Expression right) {
            this.Operator = Operator;
            this.right = right;
        }

        final Token Operator;
        final Expression right;
    }
}
