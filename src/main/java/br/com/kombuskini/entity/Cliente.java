package br.com.kombuskini.entity;

public class Cliente {
        private int id;
        private String nome;
        private String telefone;
        private String instagram;
        private String email;

        public Cliente(int id, String nome, String telefone, String instagram, String email) {
            this.id = id;
            this.nome = nome;
            this.telefone = telefone;
            this.instagram = instagram;
            this.email = email;
            validar();
        }

        public final void validar() {
            if (nome == null || nome.trim().isEmpty()) {
                throw new IllegalArgumentException("O nome do cliente é obrigatório.");
            }

            int informacoesPreenchidas = 1; // nome

            if (telefone != null && !telefone.trim().isEmpty()) {
                informacoesPreenchidas++;
            }

            if (instagram != null && !instagram.trim().isEmpty()) {
                informacoesPreenchidas++;
            }

            if (email != null && !email.trim().isEmpty()) {
                informacoesPreenchidas++;
            }

            if (informacoesPreenchidas < 2) {
                throw new IllegalArgumentException(
                        "O cliente deve ter nome e pelo menos um canal de contato: telefone, Instagram ou e-mail."
                );
            }
        }

        public int getId() {
            return id;
        }

        public String getNome() {
            return nome;
        }

        public String getTelefone() {
            return telefone;
        }

        public String getInstagram() {
            return instagram;
        }

        public String getEmail() {
            return email;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setNome(String nome) {
            this.nome = nome;
            validar();
        }

        public void setTelefone(String telefone) {
            this.telefone = telefone;
            validar();
        }

        public void setInstagram(String instagram) {
            this.instagram = instagram;
            validar();
        }

        public void setEmail(String email) {
            this.email = email;
            validar();
        }
}
