PGDMP                      |            java_webapi    16.0    16.2 	    �           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            �           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            �           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            �           1262    33311    java_webapi    DATABASE        CREATE DATABASE java_webapi WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Russian_Russia.1251';
    DROP DATABASE java_webapi;
                postgres    false            �            1259    33316    notifications    TABLE     F  CREATE TABLE public.notifications (
    id bigint NOT NULL,
    email character varying(255) NOT NULL,
    auditory_id bigint NOT NULL,
    meeting_time timestamp without time zone NOT NULL,
    uuid uuid NOT NULL,
    status boolean DEFAULT false NOT NULL,
    message character varying(255),
    meeting_id uuid NOT NULL
);
 !   DROP TABLE public.notifications;
       public         heap    postgres    false            �            1259    33315    notifications_id_seq    SEQUENCE     �   ALTER TABLE public.notifications ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.notifications_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    216            �          0    33316    notifications 
   TABLE DATA           p   COPY public.notifications (id, email, auditory_id, meeting_time, uuid, status, message, meeting_id) FROM stdin;
    public          postgres    false    216   �	       �           0    0    notifications_id_seq    SEQUENCE SET     C   SELECT pg_catalog.setval('public.notifications_id_seq', 65, true);
          public          postgres    false    215            R           2606    33322     notifications notifications_pkey 
   CONSTRAINT     ^   ALTER TABLE ONLY public.notifications
    ADD CONSTRAINT notifications_pkey PRIMARY KEY (id);
 J   ALTER TABLE ONLY public.notifications DROP CONSTRAINT notifications_pkey;
       public            postgres    false    216            �   �   x��ѱq1��_�� �T�B� $���KNd��/�?���68�4���߿{��/�����m�<��Ā�>��(w��H9Z�av8� ���j)�pZ)�f�K\�.��]�l�(ۦ�*/s�^�qS1蓢ͩM����U~�KHG�����[�#N�b��>��U�!R!�P�p�1KR���2p��\�cT5�
�
K�1xT�ݰ"�i��m�?�j�3     