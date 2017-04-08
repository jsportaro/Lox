package com.lox;

public class Interpreter implements Expression.Visitor<Object> {

    void interpret(Expression expression) {
        try {
            Object value = evaluate(expression);
            System.out.println(stringify(value));
        } catch (RuntimeError error) {
            Lox.runtimeError(error);
        }
    }

    @Override
    public Object visitBinaryExpression(Expression.Binary expression) {
        Object left = evaluate(expression.left);
        Object right = evaluate(expression.right);

        switch (expression.operator.type) {
            case GREATER:
                checkNumberOperand(expression.operator, left, right);
                return (double)left > (double)right;
            case LESS:
                checkNumberOperand(expression.operator, left, right);
                return (double)left < (double)right;
            case GREATER_EQUAL:
                checkNumberOperand(expression.operator, left, right);
                return (double)left >= (double)right;
            case LESS_EQUAL:
                checkNumberOperand(expression.operator, left, right);
                return (double)left <= (double)right;
            case EQUAL:
                checkNumberOperand(expression.operator, left, right);
                return isEqual(left, right);
            case BANG_EQUAL:
                checkNumberOperand(expression.operator, left, right);
                return !isEqual(left, right);
            case MINUS:
                checkNumberOperand(expression.operator, left, right);
                return (double)left - (double)right;
            case SLASH:
                checkNumberOperand(expression.operator, left, right);
                return (double)left / (double)right;
            case STAR:
                checkNumberOperand(expression.operator, left, right);
                return (double)left * (double)right;
            case PLUS:
                if (left instanceof Double && right instanceof Double)
                    return (double)left + (double)right;
                else if (left instanceof String && right instanceof String)
                    return (String)left + (String)right;
                else
                    throw new RuntimeError(expression.operator,
                            "Operands must be two numbers or two strings");
        }

        // Unreachable
        return null;
    }

    @Override
    public Object visitGroupingExpression(Expression.Grouping expression) {
        return evaluate(expression.expression);
    }

    @Override
    public Object visitLiteralExpression(Expression.Literal expression) {
        return expression.value;
    }

    @Override
    public Object visitUnaryExpression(Expression.Unary expression) {
        Object right = evaluate(expression.right);

        switch (expression.operator.type) {
            case MINUS:
                return -(double)right;
            case BANG:
                return !isTrue(right);
        }

        //  Unreachable.
        return null;
    }

    private void checkNumberOperand(Token operator, Object operand) {
        if (operand instanceof Double) {
            return;
        }
        throw new RuntimeError(operator, "Operand must be a number");
    }

    private void checkNumberOperand(Token operator, Object left, Object right) {
        if (left instanceof Double && right instanceof Double){
            return;
        }
        throw new RuntimeError(operator, "Operands must be numbers");
    }

    private Object evaluate(Expression expression) {
        return expression.accept(this);
    }

    private boolean isTrue(Object object) {
        if (object == null) return false;
        if (object instanceof Boolean) return (boolean)object;
        return true;
    }

    private boolean isEqual(Object left, Object right) {
        if (left == null && right == null) return true;
        if (left == null) return false;

        return left.equals(right);
    }

    private String stringify(Object object) {
        if (object == null)
            return "nil";

        if (object instanceof Double){
            String text = object.toString();
            if (text.endsWith(".0")) {
                text = text.substring(0, text.length() - 2);
            }

            return  text;
        }

        return object.toString();
    }
}
