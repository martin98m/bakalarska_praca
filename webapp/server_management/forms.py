from django import forms


class CommandForm(forms.Form):
    command = forms.CharField(max_length=100, required=True)


class LoginForm(forms.Form):
    username = forms.CharField(max_length=20, required=True)
    password = forms.CharField(max_length=20, required=True, widget=forms.PasswordInput())


class CommandSave(forms.Form):
    # username = forms.CharField(max_length=100, required=True)
    commandSave = forms.CharField(max_length=100, required=True)
    target = forms.CharField(max_length=100, required=True)


